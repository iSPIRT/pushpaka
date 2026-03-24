#!/usr/bin/env bash
# setup.sh — wire custom/ into the QGC source tree and configure the build.
# Run once after cloning (or automatically via devcontainer postCreateCommand).

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
QGC_DIR="${SCRIPT_DIR}/qgroundcontrol"
CUSTOM_LINK="${QGC_DIR}/custom"
BUILD_DIR="${SCRIPT_DIR}/build"

echo "[pushpaka-qgc] Initialising QGC submodule..."
git -C "${SCRIPT_DIR}/.." submodule update --init --recursive qgc-plugin/qgroundcontrol

echo "[pushpaka-qgc] Linking custom/ into QGC source tree..."
if [ -L "${CUSTOM_LINK}" ]; then
    echo "  symlink already exists, skipping"
elif [ -e "${CUSTOM_LINK}" ]; then
    echo "  ERROR: ${CUSTOM_LINK} exists but is not a symlink — remove it and re-run"
    exit 1
else
    ln -s "${SCRIPT_DIR}/custom" "${CUSTOM_LINK}"
    echo "  created: ${CUSTOM_LINK} -> ${SCRIPT_DIR}/custom"
fi

echo "[pushpaka-qgc] Configuring CMake build..."
cmake -S "${QGC_DIR}" -B "${BUILD_DIR}" \
    -G Ninja \
    -DCMAKE_BUILD_TYPE=Debug

echo ""
echo "[pushpaka-qgc] Setup complete."
echo "  To build: cmake --build ${BUILD_DIR} --parallel"
echo "  Binary:   ${BUILD_DIR}/QGroundControl"
