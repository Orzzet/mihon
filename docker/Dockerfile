# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl wget unzip git ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Android SDK setup
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools"

ARG ANDROID_CMDLINE_VERSION=11076708

RUN mkdir -p $ANDROID_SDK_ROOT/cmdline-tools && \
    curl -Lo /tmp/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_CMDLINE_VERSION}_latest.zip && \
    unzip /tmp/cmdline-tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools && \
    mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip && \
    yes | sdkmanager --licenses >/dev/null && \
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

WORKDIR /workspace

CMD ["bash"]
