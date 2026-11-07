#!/usr/bin/env bash
set -uo pipefail
cd offline-demo
./gradlew --no-daemon :app:connectedDebugAndroidTest
test_status=$?
mkdir -p screenshots
adb pull /data/local/tmp/smart-routine-demo/. screenshots/ || exit 1
adb logcat -d -s AndroidRuntime:E > screenshots/emulator-errors.txt
exit "$test_status"
