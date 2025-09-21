# Multi-stage build for Flutter app
FROM debian:buster-slim AS build

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    git \
    unzip \
    xz-utils \
    zip \
    libglu1-mesa \
    && rm -rf /var/lib/apt/lists/*

# Install Flutter
ENV FLUTTER_HOME=/opt/flutter
ENV PATH="$FLUTTER_HOME/bin:$PATH"

RUN git clone https://github.com/flutter/flutter.git -b stable $FLUTTER_HOME
RUN flutter doctor -v

# Set up app directory
WORKDIR /app

# Copy pubspec files first for better caching
COPY pubspec.yaml pubspec.lock ./

# Get dependencies
RUN flutter pub get

# Copy app source code
COPY . .

# Build the app
RUN flutter build apk --release --target-platform android-arm64

# Runtime stage
FROM alpine:latest

# Copy built APK
COPY --from=build /app/build/app/outputs/flutter-apk/app-release.apk /app.apk

# Expose port for adb if needed
EXPOSE 5037

# Default command - just show the APK location
CMD echo "Built APK available at /app.apk" && \
    echo "To install on device: adb install /app.apk" && \
    echo "Or copy it out of the container"