# Java 17 + Maven devcontainer base
FROM mcr.microsoft.com/devcontainers/java:17

# ── Maven ─────────────────────────────────────────────────────────────────────
USER root
RUN apt-get update && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

# ── Python / pip (for mkdocs + sitl-bridge deps) ─────────────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends python3 python3-pip \
    && rm -rf /var/lib/apt/lists/* \
    && ln -s /usr/bin/python3 /usr/local/bin/python \
    && ln -s /usr/bin/pip3 /usr/local/bin/pip

USER vscode
WORKDIR /workspace
