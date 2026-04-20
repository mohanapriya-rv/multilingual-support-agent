# Mobile App (Capacitor WebView)

This wraps the existing `index.html` UI into native iOS/Android apps using [Capacitor](https://capacitorjs.com/), and uses [`@capacitor-community/speech-recognition`](https://github.com/capacitor-community/speech-recognition) for native speech-to-text on mobile. The web build still uses the browser's `webkitSpeechRecognition`.

## Prerequisites

- Node.js 18+
- iOS: Xcode + CocoaPods (`sudo gem install cocoapods`)
- Android: Android Studio + JDK 17

## Install dependencies

```bash
cd frontend
npm install
```

## Add platforms

```bash
npm run add:ios      # adds iOS project
npm run add:android  # adds Android project
```

This runs `prepare:web` which copies `index.html` + `assets/` into `www/` (Capacitor's web directory).

## Inject Capacitor runtime into the WebView

Capacitor automatically injects `window.Capacitor` + plugin bridges at runtime when the app loads inside a native WebView, so you do **not** need a script tag in `index.html`. The existing STT code checks `window.Capacitor?.isNativePlatform()` and falls back to the browser API for regular web.

## Configure native permissions

### iOS (`ios/App/App/Info.plist`)

Add:

```xml
<key>NSMicrophoneUsageDescription</key>
<string>Zivra needs microphone access for voice input.</string>
<key>NSSpeechRecognitionUsageDescription</key>
<string>Zivra uses speech recognition to transcribe voice.</string>
```

### Android (`android/app/src/main/AndroidManifest.xml`)

Make sure these exist:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
<queries>
  <intent>
    <action android:name="android.speech.RecognitionService" />
  </intent>
</queries>
```

## Sync and run

```bash
npm run sync          # rebuilds www/ and syncs both platforms
npm run open:ios      # opens Xcode
npm run open:android  # opens Android Studio
```

Or run directly on a connected device / simulator:

```bash
npm run run:ios
npm run run:android
```

## Backend URL

The frontend uses:

```js
const API_BASE = window.location.hostname === 'localhost'
  ? 'http://localhost:8080/api'
  : (window.API_URL || 'https://support-agent-api.onrender.com/api');
```

Inside a Capacitor app, `window.location.hostname` is `localhost` by default, so it will try `http://localhost:8080`. To point to your deployed backend for mobile builds, either:

- Set `window.API_URL` via a small inline script in `index.html` before the main script, or
- Replace the API_BASE logic to always use the production URL for native.

## Notes

- Capacitor uses WKWebView (iOS) / WebView (Android). All your existing CSS/JS runs unchanged.
- The speech plugin bridges to Android `SpeechRecognizer` and iOS `SFSpeechRecognizer` - regional languages (hi-IN, ta-IN, te-IN, kn-IN, en-US) are supported if installed on the device.
- For best results on iOS, test on a physical device (simulator mic can be flaky).
