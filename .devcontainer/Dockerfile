# Java 17 + Maven devcontainer base
FROM mcr.microsoft.com/devcontainers/java:17

# ── Maven ─────────────────────────────────────────────────────────────────────
USER root
RUN apt-get update && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

# ── Python / mkdocs-material (venv to satisfy PEP 668) ───────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends python3 python3-venv \
    && rm -rf /var/lib/apt/lists/* \
    && python3 -m venv /opt/mkdocs-venv \
    && /opt/mkdocs-venv/bin/pip install --no-cache-dir mkdocs mkdocs-material \
    && ln -s /opt/mkdocs-venv/bin/mkdocs /usr/local/bin/mkdocs

USER vscode
WORKDIR /workspace
