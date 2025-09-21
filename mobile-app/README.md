# Notes Mobile App

A Flutter mobile app for managing notes with offline-first capabilities.

## Features

- ✅ Authentication (Login/Register screens)
- ✅ Notes list and detail view
- ✅ Note creation and editing with Markdown preview
- ✅ Offline-first architecture with SQLite
- ✅ Background sync operations
- ✅ Pull-to-refresh and empty states
- ✅ Online/offline indicators

## Setup

1. Install Flutter: https://flutter.dev/docs/get-started/install
2. Install Android Studio and set up emulator
3. Run `flutter pub get` to install dependencies
4. Use the launch script: `./launch_android.sh`

## Build Scripts

- `launch_android.sh` - Launches the app on Android emulator/device

## Architecture

- **State Management**: Riverpod
- **Database**: SQLite with sqflite
- **Offline Sync**: Operation queue with background sync
- **Conflict Resolution**: Last-Write-Wins strategy

## Offline Strategy

1. **Read**: Always from local cache
2. **Write**: Local first, then added to sync queue
3. **Sync**: Background process when connectivity is available
4. **Conflicts**: Last write wins - newer timestamps overwrite older ones