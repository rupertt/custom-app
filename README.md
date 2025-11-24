## Sample Java 17 SparkJava App

This is a minimal Java 17 HTTP service using [SparkJava](https://sparkjava.com/) with a single endpoint.

- **Port**: `8080`
- **Endpoint**: `GET /message`
- **Response**:

```json
{ "message": "Hello from the sample Java app!" }
```

### Requirements
- Java 17 (JDK 17+)
- Gradle Wrapper (included) or Gradle 8+ installed locally to generate the wrapper once

Check Java:

```bash
java -version
```

If you need Java 17:
- WSL (Ubuntu/Debian): `sudo apt-get update && sudo apt-get install -y openjdk-17-jdk`
- macOS (Homebrew): `brew install openjdk@17`
- Windows (winget): `winget install EclipseAdoptium.Temurin.17.JDK`

### Build

```bash
./gradlew build
```

On Windows PowerShell:

```powershell
.\gradlew.bat build
```

If the Gradle wrapper scripts are not present yet, generate them once with a local Gradle 8+ install:

```bash
gradle wrapper
```

### Run

```bash
./gradlew run
```

On Windows PowerShell:

```powershell
.\gradlew.bat run
```

If you see a permission error on WSL/macOS:

```bash
chmod +x gradlew && ./gradlew run
```

Then visit:

```
http://localhost:8080/message
```

You should see:

```json
{ "message": "Hello from the sample Java app!" }
```

You can also test with curl:

```bash
curl -s http://localhost:8080/message | jq .
```

### Project Structure

```
.
├─ build.gradle
├─ settings.gradle
├─ src
│  └─ main
│     └─ java
│        └─ com
│           └─ example
│              └─ App.java
└─ .gitignore
```

### Git & GitHub

Initialize and make the first commit:

```bash
git init
git add .
git commit -m "Initial commit: Java 17 SparkJava sample app"
```

Push to GitHub using the GitHub CLI (recommended, non-interactive):

```bash
# Ensure you're authenticated first: gh auth login
gh repo create custom-app --private --source . --remote origin --push --description "Sample Java 17 SparkJava app (Java 17 + SparkJava)"
```

Manual push (if not using GitHub CLI):

```bash
# Create an empty repo on GitHub (no README/license) named custom-app, then:
git remote add origin https://github.com/<your-username>/custom-app.git
# Option A) Keep current branch name (master):
git push -u origin master
# Option B) Rename to main first:
# git branch -M main && git push -u origin main
```


