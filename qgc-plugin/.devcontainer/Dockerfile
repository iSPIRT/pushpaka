FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV QT_VERSION=6.6.3
ENV QT_DIR=/opt/Qt/${QT_VERSION}/gcc_64

# System deps for QGC v5 build
RUN apt-get update && apt-get install -y \
    build-essential cmake ninja-build git \
    python3 python3-pip \
    # Qt platform / rendering
    libgl1-mesa-dev libvulkan-dev libxkbcommon-dev \
    libx11-dev libxext-dev libxrandr-dev libxcursor-dev libxi-dev \
    # GStreamer (QGC video)
    libgstreamer1.0-dev libgstreamer-plugins-base1.0-dev \
    # Misc
    curl wget unzip libssl-dev \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Install Qt via aqtinstall
RUN pip3 install aqtinstall && \
    aqt install-qt linux desktop ${QT_VERSION} gcc_64 \
        --outputdir /opt/Qt \
        -m qt5compat qtcharts qtlocation qtmultimedia \
           qtnetworkauth qtpositioning qtquick3d \
           qtspeech qtwebsockets qtshadertools \
    && rm -rf /tmp/aqt*

ENV PATH="${QT_DIR}/bin:${PATH}"
ENV CMAKE_PREFIX_PATH="${QT_DIR}"

WORKDIR /workspace
