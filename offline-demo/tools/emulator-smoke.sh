#!/usr/bin/env bash
set -uo pipefail
cd offline-demo
adb shell input keyevent KEYCODE_WAKEUP
adb shell wm dismiss-keyguard
adb shell svc power stayon true
adb shell settings put system screen_off_timeout 2147483647
./gradlew --no-daemon :app:connectedDebugAndroidTest
test_status=$?
mkdir -p screenshots
adb exec-out screencap -p > screenshots/emulator-final.png
adb shell dumpsys window > screenshots/emulator-window.txt
adb pull /data/local/tmp/smart-routine-demo/. screenshots/
capture_status=$?
adb logcat -d -s AndroidRuntime:E > screenshots/emulator-errors.txt
if [ "$test_status" -eq 0 ] && [ "$capture_status" -ne 0 ]; then exit 1; fi
exit "$test_status"
