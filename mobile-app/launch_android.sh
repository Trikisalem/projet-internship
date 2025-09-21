#!/bin/bash

# Launch Android emulator or device for Notes app

echo "Building and launching Notes app on Android..."

# Check if Flutter is installed
if ! command -v flutter &> /dev/null; then
    echo "Flutter is not installed. Please install Flutter first."
    exit 1
fi

# Check if Android device is connected
DEVICES=$(flutter devices | grep "android")
if [ -z "$DEVICES" ]; then
    echo "No Android devices found. Please start an emulator or connect a device."
    echo "Starting Android emulator..."
    emulator -avd Pixel_4_API_30 &> /dev/null &
    sleep 10
fi

# Install dependencies
flutter pub get

# Run the app
flutter run --target=lib/main.dart