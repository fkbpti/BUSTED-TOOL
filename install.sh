#!/data/data/com.termux/files/usr/bin/bash
set -e

echo "[+] BUSTED TOOL installer"
pkg update -y
pkg install -y openjdk-17

if command -v gradle >/dev/null 2>&1; then
    gradle build
else
    echo "[!] Gradle is not installed."
    echo "[+] Install it with: pkg install gradle"
    exit 1
fi

echo "[+] Build complete."
echo "[+] JAR: build/libs/BUSTED-TOOL-1.0.0.jar"
