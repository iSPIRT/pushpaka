FROM mcr.microsoft.com/devcontainers/base:ubuntu-22.04

# ── System deps ───────────────────────────────────────────────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl zip unzip git python3 python3-pip \
    && rm -rf /var/lib/apt/lists/*

# ── SDKMAN → Java 17 + Maven 3 ───────────────────────────────────────────────
USER vscode
RUN curl -s "https://get.sdkman.io" | bash && \
    bash -c "source /home/vscode/.sdkman/bin/sdkman-init.sh && \
    sdk install java 17.0.10-tem && \
    sdk install maven 3.9.6"

# ── Python / mkdocs-material ──────────────────────────────────────────────────
USER root
RUN pip3 install --no-cache-dir mkdocs mkdocs-material

USER vscode
WORKDIR /workspace
