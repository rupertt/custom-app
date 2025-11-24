## Sample Java 17 SparkJava App

Minimal HTTP service using SparkJava on port 8080. It exposes:
- GET `/` simple page that polls `/message` and a “Track Event” button
- GET `/message` returns a JSON message

### Run instructions
1) Update keys in `src/main/java/com/example/App.java`:
   - `SDK_KEY` (your LaunchDarkly server SDK key)
   - `FEATURE_FLAG_KEY` (your flag key)

2) Build:
```bash
./gradlew build
# Windows PowerShell: .\gradlew.bat build
```

3) Run:
```bash
./gradlew run
# Windows PowerShell: .\gradlew.bat run
```

4) Access:
- Browser: `http://localhost:8080`
- API: `http://localhost:8080/message`
