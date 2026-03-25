#!/usr/bin/env bash
# e2e-smoke.sh — Pushpaka end-to-end simulation bootstrap
#
# Boots the full stack, runs API smoke checks, launches QGC, then prints the
# manual QGC checklist. This is the entry point for running a local simulation.
#
# Usage:
#   ./e2e-smoke.sh              # boot + smoke + launch QGC
#   ./e2e-smoke.sh --no-qgc     # boot + smoke only (no QGC launch)
#   ./e2e-smoke.sh --teardown   # stop services and docker stack
#
# Requirements: docker, mvn (Java 17), curl, jq

set -euo pipefail

# ── Resolve repo root ──────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../../../../.." && pwd)"
REF_IMPL_DIR="$REPO_ROOT/reference-implementation"
QGC_PLUGIN_DIR="$REPO_ROOT/qgc-plugin"
QGC_DIR="$QGC_PLUGIN_DIR/qgroundcontrol"
QGC_BUILD_DIR="$QGC_PLUGIN_DIR/build"
DEVCONTAINER_DIR="$REPO_ROOT/.devcontainer"
LOG_DIR="$REF_IMPL_DIR/tmp"
mkdir -p "$LOG_DIR"

# ── Config (matches .devcontainer/.env defaults) ───────────────────────────────
KEYCLOAK_URL="${KEYCLOAK_URL:-http://localhost:18080}"
REGISTRY_URL="${REGISTRY_URL:-http://localhost:8082}"
FLIGHT_AUTH_URL="${FLIGHT_AUTH_URL:-http://localhost:8083}"
KEYCLOAK_REALM="pushpaka"
KEYCLOAK_CLIENT="backend"
PILOT_USER="${PUSHPAKA_USER:-test.pilot.0@test.com}"
PILOT_PASS="${PUSHPAKA_PASSWORD:-test}"

LAUNCH_QGC=true
TEARDOWN=false

for arg in "$@"; do
  case $arg in
    --no-qgc)   LAUNCH_QGC=false ;;
    --teardown) TEARDOWN=true ;;
  esac
done

# ── Colours ────────────────────────────────────────────────────────────────────
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
RESET='\033[0m'

PASS="${GREEN}PASS${RESET}"
FAIL="${RED}FAIL${RESET}"
WARN="${YELLOW}WARN${RESET}"

# ── Tracking ───────────────────────────────────────────────────────────────────
RESULTS=()
FAILED=0

record() {
  local label="$1" status="$2" detail="${3:-}"
  if [[ "$status" == "PASS" ]]; then
    RESULTS+=("  ${GREEN}✓${RESET}  $label")
  elif [[ "$status" == "WARN" ]]; then
    RESULTS+=("  ${YELLOW}!${RESET}  $label${detail:+  ($detail)}")
  else
    RESULTS+=("  ${RED}✗${RESET}  $label${detail:+  ($detail)}")
    FAILED=$((FAILED + 1))
  fi
}

# ── Helpers ────────────────────────────────────────────────────────────────────
wait_for_port() {
  local name="$1" host="$2" port="$3" timeout="${4:-60}"
  local elapsed=0
  printf "  Waiting for %s (port %s)" "$name" "$port"
  while ! curl -sf "$host:$port" >/dev/null 2>&1; do
    if (( elapsed >= timeout )); then
      echo ""
      return 1
    fi
    printf "."
    sleep 2
    elapsed=$((elapsed + 2))
  done
  echo " ready"
  return 0
}

wait_for_url() {
  local name="$1" url="$2" timeout="${3:-60}"
  local elapsed=0
  printf "  Waiting for %s" "$name"
  while ! curl -sf "$url" >/dev/null 2>&1; do
    if (( elapsed >= timeout )); then
      echo ""
      return 1
    fi
    printf "."
    sleep 2
    elapsed=$((elapsed + 2))
  done
  echo " ready"
  return 0
}

# ── Teardown ───────────────────────────────────────────────────────────────────
if [[ "$TEARDOWN" == "true" ]]; then
  echo -e "${BOLD}Teardown${RESET}"
  if [[ -f "$LOG_DIR/registry.pid" ]]; then
    kill "$(cat "$LOG_DIR/registry.pid")" 2>/dev/null && echo "  Registry stopped" || true
    rm -f "$LOG_DIR/registry.pid"
  fi
  if [[ -f "$LOG_DIR/flightauth.pid" ]]; then
    kill "$(cat "$LOG_DIR/flightauth.pid")" 2>/dev/null && echo "  Flight auth stopped" || true
    rm -f "$LOG_DIR/flightauth.pid"
  fi
  cd "$REPO_ROOT"
  docker compose -f "$DEVCONTAINER_DIR/docker-compose.yml" down
  echo "  Docker stack stopped"
  exit 0
fi

# ══════════════════════════════════════════════════════════════════════════════
echo ""
echo -e "${BOLD}${CYAN}╔══════════════════════════════════════════╗${RESET}"
echo -e "${BOLD}${CYAN}║   Pushpaka E2E Smoke Test                ║${RESET}"
echo -e "${BOLD}${CYAN}╚══════════════════════════════════════════╝${RESET}"
echo ""

# ── Phase 1: Docker stack ──────────────────────────────────────────────────────
echo -e "${BOLD}Phase 1 — Boot docker stack${RESET}"

cd "$REPO_ROOT"
docker compose -f "$DEVCONTAINER_DIR/docker-compose.yml" up -d --remove-orphans
echo "  Containers started"

# Wait for Postgres
if wait_for_url "Postgres" "http://localhost:15432" 30 2>/dev/null || \
   docker exec "$(docker compose -f "$DEVCONTAINER_DIR/docker-compose.yml" ps -q database 2>/dev/null)" \
     pg_isready -U postgres >/dev/null 2>&1; then
  record "Postgres" "PASS"
else
  # Fallback: just wait a few seconds and assume it's up
  sleep 5
  record "Postgres" "WARN" "could not verify readiness — proceeding"
fi

# Wait for Keycloak
if wait_for_url "Keycloak" "$KEYCLOAK_URL/health/ready" 90; then
  record "Keycloak" "PASS"
else
  record "Keycloak" "FAIL" "did not become ready within 90s"
  echo -e "\n${RED}Keycloak failed to start. Check docker logs.${RESET}"
  docker compose -f "$DEVCONTAINER_DIR/docker-compose.yml" logs keycloak | tail -20
  exit 1
fi

echo ""

# ── Phase 2: Java services ─────────────────────────────────────────────────────
echo -e "${BOLD}Phase 2 — Start Java services${RESET}"
cd "$REF_IMPL_DIR"

# Registry
if lsof -ti:8082 >/dev/null 2>&1; then
  echo "  Registry already running on port 8082"
  record "Registry startup" "PASS"
else
  echo "  Starting Registry (logs → tmp/registry.log)"
  SPRING_PROFILES_ACTIVE=registry mvn -q compile exec:java \
    -Dexec.mainClass="in.ispirt.pushpaka.registry.RegistryService" \
    > "$LOG_DIR/registry.log" 2>&1 &
  echo $! > "$LOG_DIR/registry.pid"

  if wait_for_url "Registry" "$REGISTRY_URL/actuator/health" 90 || \
     wait_for_url "Registry" "$REGISTRY_URL" 30; then
    record "Registry startup" "PASS"
  else
    record "Registry startup" "FAIL" "check tmp/registry.log"
    echo -e "\n${RED}Registry failed to start.${RESET} Last 20 lines of log:"
    tail -20 "$LOG_DIR/registry.log"
    exit 1
  fi
fi

# Flight auth
if lsof -ti:8083 >/dev/null 2>&1; then
  echo "  Flight auth already running on port 8083"
  record "Flight auth startup" "PASS"
else
  echo "  Starting Flight Auth (logs → tmp/flightauth.log)"
  SPRING_PROFILES_ACTIVE=flightauthorisation mvn -q compile exec:java \
    -Dexec.mainClass="in.ispirt.pushpaka.flightauthorisation.FlightAuthorisationService" \
    > "$LOG_DIR/flightauth.log" 2>&1 &
  echo $! > "$LOG_DIR/flightauth.pid"

  if wait_for_url "Flight Auth" "$FLIGHT_AUTH_URL/actuator/health" 90 || \
     wait_for_url "Flight Auth" "$FLIGHT_AUTH_URL" 30; then
    record "Flight auth startup" "PASS"
  else
    record "Flight auth startup" "FAIL" "check tmp/flightauth.log"
    echo -e "\n${RED}Flight auth failed to start.${RESET} Last 20 lines of log:"
    tail -20 "$LOG_DIR/flightauth.log"
    exit 1
  fi
fi

echo ""

# ── Phase 3: API smoke checks ──────────────────────────────────────────────────
echo -e "${BOLD}Phase 3 — API smoke checks${RESET}"

# Get token
echo "  Fetching Keycloak token (user: $PILOT_USER)"
TOKEN_RESPONSE=$(curl -sf -X POST \
  "$KEYCLOAK_URL/realms/$KEYCLOAK_REALM/protocol/openid-connect/token" \
  -d "grant_type=password&client_id=$KEYCLOAK_CLIENT" \
  -d "username=$PILOT_USER&password=$PILOT_PASS" 2>&1) || true

TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.access_token // empty' 2>/dev/null)
if [[ -n "$TOKEN" ]]; then
  record "Keycloak token fetch" "PASS"
else
  record "Keycloak token fetch" "FAIL" "check user/password or Keycloak realm config"
  echo -e "\n${RED}Could not obtain token. Cannot continue API checks.${RESET}"
  echo "Response: $TOKEN_RESPONSE"
  FAILED=$((FAILED + 1))
  TOKEN=""
fi

AUTH_HEADER="Authorization: Bearer $TOKEN"

if [[ -n "$TOKEN" ]]; then
  # GET /api/v1/uas/find
  UAS_RESP=$(curl -sf -H "$AUTH_HEADER" "$REGISTRY_URL/api/v1/uas/find" 2>&1) || true
  if echo "$UAS_RESP" | jq -e '. | arrays' >/dev/null 2>&1; then
    UAS_COUNT=$(echo "$UAS_RESP" | jq 'length')
    UAS_ID=$(echo "$UAS_RESP" | jq -r '.[0].id // empty')
    record "GET /api/v1/uas/find" "PASS"
    echo "  Found $UAS_COUNT UAS entries"
  else
    record "GET /api/v1/uas/find" "FAIL" "$UAS_RESP"
  fi

  # GET /api/v1/pilots/me
  PILOT_RESP=$(curl -sf -H "$AUTH_HEADER" "$REGISTRY_URL/api/v1/pilots/me" 2>&1) || true
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -H "$AUTH_HEADER" "$REGISTRY_URL/api/v1/pilots/me" 2>/dev/null)
  if [[ "$HTTP_STATUS" == "200" ]]; then
    PILOT_ID=$(echo "$PILOT_RESP" | jq -r '.id // empty')
    record "GET /api/v1/pilots/me" "PASS"
    echo "  Pilot UUID: $PILOT_ID"
  elif [[ "$HTTP_STATUS" == "404" ]]; then
    PILOT_ID=""
    record "GET /api/v1/pilots/me" "WARN" "404 — Keycloak user not linked to a seeded Pilot; run DemoScenario1 first"
  else
    PILOT_ID=""
    record "GET /api/v1/pilots/me" "FAIL" "HTTP $HTTP_STATUS"
  fi

  # POST /api/v1/flightPlan + GET AUT (only if we have pilot + UAS)
  if [[ -n "$PILOT_ID" && -n "$UAS_ID" ]]; then
    FP_ID=$(uuidgen 2>/dev/null || python3 -c "import uuid; print(uuid.uuid4())")
    START="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
    END="$(date -u -v+30M +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -u -d '+30 minutes' +%Y-%m-%dT%H:%M:%SZ)"

    FP_RESP=$(curl -sf -X POST "$FLIGHT_AUTH_URL/api/v1/flightPlan" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{\"id\":\"$FP_ID\",\"pilot\":{\"id\":\"$PILOT_ID\"},\"uas\":{\"id\":\"$UAS_ID\"},\"start_time\":\"$START\",\"end_time\":\"$END\"}" \
      2>&1) || true
    FP_RETURNED_ID=$(echo "$FP_RESP" | jq -r '.id // empty' 2>/dev/null)

    if [[ -n "$FP_RETURNED_ID" ]]; then
      record "POST /api/v1/flightPlan" "PASS"

      AUT_RESP=$(curl -sf -H "$AUTH_HEADER" \
        "$FLIGHT_AUTH_URL/api/v1/airspace-usage-tokens/by-flight-plan/$FP_RETURNED_ID" \
        2>&1) || true
      SIGNED_JWT=$(echo "$AUT_RESP" | jq -r '.signed_jwt // empty' 2>/dev/null)
      if [[ -n "$SIGNED_JWT" ]]; then
        record "GET /airspace-usage-tokens/by-flight-plan/{id}" "PASS"
        echo "  AUT signed_jwt: ${SIGNED_JWT:0:40}…"
      else
        record "GET /airspace-usage-tokens/by-flight-plan/{id}" "FAIL" "signed_jwt absent"
      fi
    else
      record "POST /api/v1/flightPlan" "FAIL" "$FP_RESP"
      record "GET /airspace-usage-tokens/by-flight-plan/{id}" "WARN" "skipped — flight plan not created"
    fi
  else
    record "POST /api/v1/flightPlan" "WARN" "skipped — need pilot + UAS in DB (run DemoScenario1)"
    record "GET /airspace-usage-tokens/by-flight-plan/{id}" "WARN" "skipped"
  fi
fi

echo ""

# ── Phase 4: Launch QGC ────────────────────────────────────────────────────────
if [[ "$LAUNCH_QGC" == "true" ]]; then
  echo -e "${BOLD}Phase 4 — Launch QGroundControl${RESET}"

  # Check plugin is wired in (custom/ symlink must exist in QGC source tree)
  CUSTOM_LINK="$QGC_DIR/custom"
  if [[ -L "$CUSTOM_LINK" && "$(readlink "$CUSTOM_LINK")" == "$QGC_PLUGIN_DIR/custom" ]]; then
    record "Pushpaka plugin wired (custom/ symlink)" "PASS"
  elif [[ -L "$CUSTOM_LINK" ]]; then
    record "Pushpaka plugin wired (custom/ symlink)" "WARN" "symlink points to unexpected target: $(readlink "$CUSTOM_LINK")"
  elif [[ -d "$CUSTOM_LINK" ]]; then
    record "Pushpaka plugin wired (custom/ symlink)" "WARN" "custom/ is a directory not a symlink — re-run qgc-plugin/setup.sh"
  else
    record "Pushpaka plugin wired (custom/ symlink)" "FAIL" "custom/ not present — run: bash $QGC_PLUGIN_DIR/setup.sh"
    echo -e "\n${RED}Plugin not wired. Run setup first:${RESET}"
    echo "  bash $QGC_PLUGIN_DIR/setup.sh"
    echo ""
  fi

  # Detect platform binary (build dir is qgc-plugin/build/, not qgc-plugin/qgroundcontrol/build/)
  if [[ "$(uname)" == "Darwin" ]]; then
    QGC_BIN="$QGC_BUILD_DIR/QGroundControl.app/Contents/MacOS/QGroundControl"
  else
    QGC_BIN="$QGC_BUILD_DIR/QGroundControl"
  fi

  if [[ -x "$QGC_BIN" ]]; then
    echo "  Launching: $QGC_BIN"
    "$QGC_BIN" &
    QGC_PID=$!
    sleep 3
    if kill -0 $QGC_PID 2>/dev/null; then
      record "QGC launch" "PASS"
    else
      record "QGC launch" "FAIL" "process exited immediately"
    fi
  else
    record "QGC launch" "WARN" "binary not found at $QGC_BUILD_DIR — build first: cmake --build $QGC_BUILD_DIR"
  fi
  echo ""
fi

# ── Results table ──────────────────────────────────────────────────────────────
echo -e "${BOLD}Results${RESET}"
echo "  ─────────────────────────────────────────────"
for r in "${RESULTS[@]}"; do
  echo -e "$r"
done
echo "  ─────────────────────────────────────────────"
echo ""

if (( FAILED > 0 )); then
  echo -e "${RED}${BOLD}$FAILED check(s) failed.${RESET} Resolve above before proceeding."
  echo ""
else
  echo -e "${GREEN}${BOLD}All automated checks passed.${RESET}"
  echo ""
fi

# ── Manual QGC checklist ───────────────────────────────────────────────────────
if [[ "$LAUNCH_QGC" == "true" ]]; then
  echo -e "${BOLD}${CYAN}Next: complete the manual QGC checklist${RESET}"
  echo ""
  echo "  1. The Pushpaka indicator appears in the QGC toolbar (grey)"
  echo "  2. Click it — a browser opens at the Keycloak login page"
  echo "  3. Log in: $PILOT_USER / $PILOT_PASS"
  echo "  4. Indicator turns amber (authenticated)"
  echo "  5. Click again — the Flight Plan Panel opens"
  echo "  6. Select a UAS, enter start/end time, click Submit"
  echo "  7. Indicator turns green (valid AUT issued)"
  echo ""
  echo -e "  Full guide: ${CYAN}docs/ref/qgc-testing.md${RESET}"
  echo ""
fi

echo -e "  Logs:  $LOG_DIR/registry.log"
echo -e "         $LOG_DIR/flightauth.log"
echo -e "  Stop:  $0 --teardown"
echo ""

exit $FAILED
