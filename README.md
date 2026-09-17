# BUSTED TOOL

![Kotlin](https://img.shields.io/badge/Kotlin-JVM-blue)
![JDK](https://img.shields.io/badge/JDK-17-orange)
![License](https://img.shields.io/badge/License-MIT-green)
![CI](https://github.com/OWNER/BUSTED-TOOL/actions/workflows/build.yml/badge.svg)


**BUSTED TOOL** is a Kotlin/JVM cybersecurity utility toolkit designed for Termux, learning, and authorized security testing.

## Features

- DNS/address lookup
- Host/IP information
- HTTP/HTTPS response-header inspection
- URL validation
- Single authorized TCP port check
- SHA-256 and SHA-512 text hashing
- Local file SHA-256 integrity checking
- Basic password-strength estimation
- CLI help/version system

## Requirements

- Java/JDK 17
- Gradle
- Termux (optional)

## Build

```bash
gradle build
```

The generated JAR will be in:

```text
build/libs/BUSTED-TOOL-1.0.0.jar
```

Run it:

```bash
java -jar build/libs/BUSTED-TOOL-1.0.0.jar help
```

## Termux

```bash
pkg update
pkg install openjdk-17 gradle
gradle build
./busted help
```

## GitHub Actions

Every push to `main`/`master` and every pull request runs `.github/workflows/build.yml`. The workflow builds the project and uploads the JAR as an artifact.

## Example

```bash
./busted dns example.com
./busted ip example.com
./busted headers https://example.com
./busted url https://example.com
./busted port 127.0.0.1 8080
./busted hash hello
./busted filehash ./test.txt
./busted password "example-password"
```

## Legal / Responsible Use

Use this toolkit only on systems, networks, files, and domains you own or have explicit permission to test. It does not provide features for credential theft, phishing, malware, DDoS, or unauthorized access.

## License

MIT License. See `LICENSE`.

## Project Structure

```text
BUSTED-TOOL/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── workflows/build.yml
├── src/main/kotlin/com/bustedtool/Main.kt
├── src/test/kotlin/com/bustedtool/MainTest.kt
├── build.gradle.kts
├── settings.gradle.kts
├── requirements.txt
├── install.sh
├── busted
├── LICENSE
├── CHANGELOG.md
├── SECURITY.md
├── CONTRIBUTING.md
├── CODE_OF_CONDUCT.md
├── NOTICE
└── VERSION
```

> Replace `OWNER` in the badge URL with your GitHub username after creating the repository.
