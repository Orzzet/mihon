# Docker development environment

This directory contains a `Dockerfile` that prepares an environment capable of building and testing the project without requiring local Android or Java installations. It installs OpenJDK 17 and the Android SDK command line tools so Gradle tasks can run.

## Build the image

```bash
docker build -t mihon-dev docker
```

## Run tests

Use the image by mounting the project directory and executing Gradle inside the container:

```bash
docker run --rm -v "$(pwd)":/workspace -w /workspace mihon-dev ./gradlew test
```

Replace `./gradlew test` with any other Gradle command as needed for development.
