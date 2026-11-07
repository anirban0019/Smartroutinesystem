#!/usr/bin/env bash
set -uo pipefail
cd offline-demo
./gradlew --no-daemon :app:connectedDebugAndroidTest
test_status=$?
mkdir -p screenshots
adb pull /sdcard/Android/data/com.example.smartroutinesystem.demo/files/screenshots/. screenshots/ || true
adb logcat -d -s AndroidRuntime:E > screenshots/emulator-errors.txt
exit "$test_status"
