# Android hobby dev container (phone-only, no emulator).
# Base already has JDK 17 + `vscode` user + sudo.
FROM mcr.microsoft.com/devcontainers/java:1-17-bookworm

ARG ANDROID_SDK_ROOT=/opt/android-sdk
ARG CMDLINE_TOOLS_VERSION=11076708
ARG API_LEVEL=34
ARG API_LEVEL_EXTRA=35
ARG BUILD_TOOLS_34=34.0.0
ARG BUILD_TOOLS_35=35.0.0

ENV ANDROID_SDK_ROOT=${ANDROID_SDK_ROOT} \
    ANDROID_HOME=${ANDROID_SDK_ROOT} \
    DEBIAN_FRONTEND=noninteractive

ENV PATH=${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools:${PATH}

# Base image ships a yarn apt source whose GPG key is expired; we don't need node/yarn.
RUN rm -f /etc/apt/sources.list.d/yarn.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        unzip wget git curl usbutils iproute2 \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools \
    && wget -q -O /tmp/cmdtools.zip "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_VERSION}_latest.zip" \
    && unzip -q /tmp/cmdtools.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools \
    && mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest \
    && rm /tmp/cmdtools.zip \
    && yes | sdkmanager --sdk_root=${ANDROID_SDK_ROOT} --licenses > /dev/null \
    && sdkmanager --sdk_root=${ANDROID_SDK_ROOT} \
        "platform-tools" \
        "platforms;android-${API_LEVEL}" \
        "platforms;android-${API_LEVEL_EXTRA}" \
        "build-tools;${BUILD_TOOLS_34}" \
        "build-tools;${BUILD_TOOLS_35}" \
    && chown -R vscode:vscode ${ANDROID_SDK_ROOT}

USER vscode
WORKDIR /workspaces

# opencode CLI for in-container AI sessions (always latest release).
# --no-modify-path: don't touch shell rc files; PATH is set via ENV below.
RUN curl -fsSL https://opencode.ai/install | bash -s -- --no-modify-path
ENV PATH="/home/vscode/.opencode/bin:${PATH}"
