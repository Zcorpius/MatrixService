#!/bin/bash

# Build matrixclient and matrixserver, copy APKs to output/

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
OUTPUT_DIR="$PROJECT_DIR/output"

TIMESTAMP=$(date +%Y%m%d_%H%M%S)

mkdir -p "$OUTPUT_DIR"

echo "==> Building matrixclient and matrixserver..."

cd "$PROJECT_DIR"
./gradlew clean :matrixclient:assembleDebug :matrixserver:assembleDebug || {
    echo "Build failed!"
    exit 1
}

CLIENT_APK="$PROJECT_DIR/matrixclient/build/outputs/apk/debug/matrixclient-debug.apk"
SERVER_APK="$PROJECT_DIR/matrixserver/build/outputs/apk/debug/matrixserver-debug.apk"

if [ -f "$CLIENT_APK" ]; then
    # Remove old client APKs, keep latest
    rm -f "$OUTPUT_DIR"/matrixclient-*.apk
    cp "$CLIENT_APK" "$OUTPUT_DIR/matrixclient-${TIMESTAMP}.apk"
    echo "==> matrixclient -> output/matrixclient-${TIMESTAMP}.apk"
else
    echo "==> matrixclient APK not found at $CLIENT_APK"
fi

if [ -f "$SERVER_APK" ]; then
    # Remove old server APKs, keep latest
    rm -f "$OUTPUT_DIR"/matrixserver-*.apk
    cp "$SERVER_APK" "$OUTPUT_DIR/matrixserver-${TIMESTAMP}.apk"
    echo "==> matrixserver -> output/matrixserver-${TIMESTAMP}.apk"
else
    echo "==> matrixserver APK not found at $SERVER_APK"
fi

echo "==> Done!"
ls -lh "$OUTPUT_DIR"