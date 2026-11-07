# Smart Routine — offline screenshot demo

This separate Android project reuses the original app's XML layouts, timetable
adapters and notice cards. The original `app/` Firebase application is unchanged.
This is a demonstration of the interface with **fictional sample data**, not a
restored Firebase backend or evidence of a currently operating service.

## Install without Android Studio

1. On this fork's **Actions** tab, open the successful **Offline demo APK** run.
2. Under **Artifacts**, download `smart-routine-offline-demo-apk` and extract it.
3. Transfer `app-debug.apk` to your Android phone and open it. Allow installation
   from that browser/file manager when prompted. Android 9 or newer is required.
4. Open **Smart Routine Demo**. Its package is `com.example.smartroutinesystem.demo`,
   so the original app remains installed. No Play Store release is needed.

## Explore and take screenshots

- **Student:** Show Routine (days/times/sections), Home (a clearly marked sample
  Saturday), Show Notices, and Profile through the overflow menu.
- **CR:** the same views plus Routine Input and Notices input. Add/update/delete
  sample classes and publish notices locally; no push notification is sent.
- **Admin:** Make CR (try roll `1900001` or `1900003`) and Remove CR (CSE / 19 / A).
  Assignment and removal affect the sample account's available edit controls.
- **Profile:** edit sample name/phone. Class, roll, role and email are fixed sample
  identity fields, not real account administration. There is no password workflow.
- Use the overflow menu to switch sample accounts or **Reset demo**. Data is
  process-local: edits survive navigation/account switching, but restart/reset
  restores examples. Do not enter real personal or confidential data.

Every screen keeps **OFFLINE DEMO · SAMPLE DATA** in the app bar. Keep that label
in screenshots; describe portfolio images as genuine app screens using demo data.
Cloud emulator captures, when tests pass, are in the `offline-demo-review` artifact.
No demo screenshot is automatically added to or published on the portfolio.

## Build and verification

The `offline-demo` branch push triggers the cloud workflow. After its first run,
the CLI can trigger it again with:

```sh
gh workflow run build-demo.yml --repo anirban0019/Smartroutinesystem --ref offline-demo
```

The build uses JDK 17, AGP 8.2.2, Gradle 8.2 and Android SDK 34 **on GitHub**.
No Android Studio, SDK, emulator or Java installation is needed on the owner's PC.
Equivalent command if an SDK is already available elsewhere:

```sh
cd offline-demo
./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
```

The workflow tests store mutations/role checks, inspects the signed APK, then runs
four instrumented smoke tests and captures real screens on an Android 14 emulator.
The demo has no Internet/network-state permission, Firebase SDK, Google Services
plugin, Firebase keys, server credentials, sign-in, or notification sending.
Only pinned actions and public Android/UI dependencies are used; no CI secrets
are required. Artifacts expire after 14 days, so save the APK locally.

Debug signing keys are generated on the cloud runner. A later build may have a
different signing key; Android may require uninstalling the old **demo** package
before installing that later demo. The original app does not need to be removed.
