## Sample Java 17 SparkJava App

This is a minimal Java 17 HTTP service using [SparkJava](https://sparkjava.com/) with a single endpoint.

- **Port**: `8080`
- **Endpoint**: `GET /message`
- **Response**:

```json
{ "message": "Hello from the sample Java app!" }
```

### Requirements
- Java 17+
- Gradle 8+ (or use the Gradle Wrapper; see below)

### Build

```bash
./gradlew build
```

On Windows PowerShell:

```powershell
.\gradlew.bat build
```

If the Gradle wrapper scripts are not present yet, generate them once with a local Gradle install:

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

Then visit:

```
http://localhost:8080/message
```

You should see:

```json
{ "message": "Hello from the sample Java app!" }
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

### Git

Initialize and make the first commit:

```bash
git init
git add .
git commit -m "Initial commit: Java 17 SparkJava sample app"
```


