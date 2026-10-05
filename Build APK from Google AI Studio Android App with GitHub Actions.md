

### **Proven Requirements**

—----------------------------------

Before using the workflow prompt, confirm that the project uses:

**AGP: 9.1.1**  
**Gradle: 9.3.1**  
**Java: 17**  
**Android module: app**  
**Build task: :app:assembleDebug**  
**APK path: app/build/outputs/apk/debug/app-debug.apk**  
**Debug keystore path: debug.keystore**  
**Artifact name: app-debug-apk**  
\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_  
You can confirm the AGP version inside:   
gradle/libs.versions.toml  
Look for:   
agp \= "9.1.1"  
\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

This workflow is tested for the current Google AI Studio Android template. Google may change the project structure or required versions in the future.

For further assistance, updated prompts, or help with any GitHub Actions build issue, **join our Skool community using the link in the video description.**

\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

**Google AI Studio Prompt to Build the Debug APK**   
\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_  
Prompt Start here:   
—---------------------  
Prepare this completed Google AI Studio Android app for automatic debug APK building with GitHub Actions.

Before creating anything, confirm that gradle/libs.versions.toml contains:

agp \= "9.1.1"

If the AGP version is not exactly 9.1.1, stop and report the detected value. Do not create the workflow.

If the AGP version is exactly 9.1.1, create only:

.github/workflows/build-apk.yml

Do not create, edit, delete, rename, format, upgrade, or replace any other file.

Do not modify the Android app, source code, design, features, package name, application ID, manifest, resources, dependencies, Gradle project files, signing configuration, or project settings.

Create a GitHub Actions workflow that:

\- Runs automatically on every push to the main branch.  
\- Also supports workflow\_dispatch.  
\- Uses ubuntu-latest.  
\- Checks out the complete repository.  
\- Sets up Temurin Java 17 using actions/setup-java.  
\- Sets up exactly Gradle 9.3.1 using gradle/actions/setup-gradle.  
\- Does not use Gradle 8.13, latest, current, or any other Gradle version.  
\- Runs all Gradle commands from the repository root.

Add a step named:

Verify Java environment

That step must:

\- Run java \-version.  
\- Print JAVA\_HOME.  
\- Confirm that the active Java major version is 17 or newer.  
\- Fail clearly if Java is lower than 17\.

Before building, add a step named:

Create temporary debug keystore

That step must:

\- Check whether ./debug.keystore already exists.  
\- Generate it only when missing.  
\- Use keytool from Java 17\.  
\- Use:  
  \- Keystore password: android  
  \- Key alias: androiddebugkey  
  \- Key password: android  
  \- Distinguished name: CN=Android Debug,O=Android,C=US  
  \- Algorithm: RSA  
  \- Key size: 2048  
  \- Validity: 10000 days  
\- Confirm that ./debug.keystore exists after generation.  
\- Fail clearly if it cannot be created.  
\- Never commit, cache, print, expose, preserve, or upload the keystore.

Build the debug APK using exactly:

gradle :app:assembleDebug \--stacktrace \--no-daemon

After the build, verify that this file exists:

app/build/outputs/apk/debug/app-debug.apk

If it is missing:

\- Search only inside app/build/outputs/apk.  
\- Print discovered APK paths.  
\- Fail clearly if no debug APK exists.  
\- Do not upload an unrelated APK.

Upload only:

app/build/outputs/apk/debug/app-debug.apk

Use actions/upload-artifact and name the artifact:

app-debug-apk

Configure artifact upload to fail when the APK is missing.

Do not upload:

\- debug.keystore  
\- credentials  
\- source code  
\- Gradle caches  
\- configuration files  
\- secrets

Do not create fake google-services.json files, Firebase files, API keys, credentials, release-signing files, or missing secrets.

Before finishing, confirm:

\- Only .github/workflows/build-apk.yml was created.  
\- AGP 9.1.1 was confirmed from gradle/libs.versions.toml.  
\- Java 17 is configured before Gradle.  
\- Gradle 9.3.1 is used exactly.  
\- The temporary debug keystore is created before the build.  
\- The build task is :app:assembleDebug.  
\- The APK path is app/build/outputs/apk/debug/app-debug.apk.  
\- The artifact name is app-debug-apk.  
\- No Android application or project file was modified.

Give me a short workflow summary.

Do not publish, commit, sync, or push to GitHub. Wait for me to do that manually.  
\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_  
Prompt End:   
—--------------  
