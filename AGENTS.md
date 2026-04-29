# Purpose

Keep this repo aligned with the current sample: a very small Micronaut app that uses Micronaut's GraalPy integration to run VADER sentiment analysis on an uploaded text file.

# Current Sample Snapshot

- Artifact and app name: `graalpy-mn-vader-v2`
- Version label: `v2`
- Framework: Micronaut 5.0.0-SNAPSHOT
- Java target: 25
- Preferred local runtime: `sdk use java 25.0.2-graal`
- GraalPy build tooling: `graalpy-maven-plugin` 25.0.2
- Micronaut GraalPy integration: `io.micronaut.graal-languages:micronaut-graalpy`
- Python package: `vaderSentiment==3.3.2`
- UI shape: one static page with upload, preview, sentiment card, raw JSON, and a clear button
- Bundled sample inputs: two short text files under `samples/`

# Pattern To Preserve

1. Serve the UI from `src/main/resources/public`.
2. Accept a multipart upload at `/api/reviews/analyze`.
3. Validate missing or empty uploads in the controller.
4. Decode uploaded bytes to plain UTF-8 text in Java.
5. Call a typed injected GraalPy module interface from Java.
6. Keep the Python module under `src/main/resources/org.graalvm.python.vfs/src/`.
7. Return a small JSON string from Python and map it into a Java record.
8. Render the preview, score, label, emoji, and raw JSON in the browser.
9. Keep a single clear action that resets both the file input and the visible output.
10. Keep native-image proxy metadata in sync with the GraalPy module interface.

# Similar-Example Rules

- Prefer the injected `@GraalPyModule` pattern over manual `Context` creation, `eval`, bindings lookup, or the polyglot `Value` API.
- Keep the Java-to-Python boundary simple: pass plain decoded text plus small scalar inputs such as the file name.
- Keep the Python side tiny and obvious. One module file and one exported function is ideal for this style of sample.
- Return JSON from Python and deserialize it into a small `@Serdeable` record on the Java side.
- If you rename the Python module, update the module file path, the `@GraalPyModule` value, and any related native-image metadata together.
- If you change the GraalPy module interface, update `src/main/resources/META-INF/native-image/proxy-config.json` to match.
- Keep `pom.xml` lean. Keep `io.micronaut.graal-languages:micronaut-graalpy` and the `graalpy-maven-plugin`.
- Do not add `org.graalvm.polyglot:python-community`.
- Do not add `org.graalvm.python:python-embedding` unless the app directly uses the embedding API.
- Pin Python packages to exact versions for reproducible demos.
- Keep the UI minimal and back-of-room readable. Small visual cues like an emoji are fine when they improve a live demo.
- Keep sample content original, short, safe for a public conference setting, and easy to read aloud.
- Do not add benchmark, load-test, multi-step orchestration, or unrelated demo paths to this repo.
- Keep Micronaut, GraalVM, GraalPy, and Java bytecode versions aligned when making runtime upgrades.

# Current Code Map

- `pom.xml`
- `src/main/java/graalpy/demo/Application.java`
- `src/main/java/graalpy/demo/ReviewController.java`
- `src/main/java/graalpy/demo/GraalPySentimentService.java`
- `src/main/java/graalpy/demo/SentimentModule.java`
- `src/main/java/graalpy/demo/ReviewAnalysisView.java`
- `src/main/resources/application.properties`
- `src/main/resources/org.graalvm.python.vfs/src/sentiment_app.py`
- `src/main/resources/META-INF/native-image/proxy-config.json`
- `src/main/resources/public/index.html`
- `src/main/resources/public/app.js`
- `src/main/resources/public/styles.css`
- `src/test/java/graalpy/demo/GraalPySentimentServiceTest.java`
- `src/test/java/graalpy/demo/ReviewControllerTest.java`
- `samples/`

# Run Commands

```bash
sdk use java 25.0.2-graal
```

```bash
./mvnw test
```

```bash
./mvnw mn:run
```

Open `http://localhost:8080`.

The first build needs network access so Maven and GraalPy can resolve dependencies, install the pinned VADER package, and resolve Micronaut 5 snapshot artifacts from the configured snapshot repository.

Executable jar:

```bash
sdk use java 25.0.2-graal
```

```bash
./mvnw package
```

```bash
java -jar target/graalpy-mn-vader-v2-0.1.jar
```

Inspect executable jar in JD-GUI:

```bash
java -jar jd-gui-1.6.6.jar
```

When JD-GUI opens, open `target/graalpy-mn-vader-v2-0.1.jar`.

Look for these paths inside the jar:

- `org.graalvm.python.vfs/src/sentiment_app.py`
- `org.graalvm.python.vfs/venv/`

See the bundled Python module, embedded GraalPy virtual filesystem, and installed Python packages packaged into the executable jar.

Native image:

```bash
sdk use java 25.0.2-graal
```

```bash
./mvnw package -Dpackaging=native-image
```

```bash
./target/graalpy-mn-vader-v2
```

# Success Criteria

- The sample stays small enough to explain in a few minutes.
- The Java -> injected GraalPy module -> VADER story is obvious in the code.
- The browser upload flow remains the main proof path.
- The UI shows the decoded text, label, score, JSON, emoji, and clear reset behavior.
- The sample can also be packaged and run as an executable jar with `java -jar`.
- The packaged jar is easy to inspect live to show the embedded GraalPy environment and files.
- JVM and native-image paths both remain available.
- The tests continue to cover the service path and the upload endpoint.
- The repo remains narrowly focused on this one sample.

# References

- Micronaut Graal Languages guide: https://micronaut-projects.github.io/micronaut-graal-languages/1.2.0/guide/
- Micronaut GraalPy Maven guide: https://guides.micronaut.io/latest/micronaut-graalpy-maven-java.html
- Micronaut GraalPy Python package guide: https://guides.micronaut.io/latest/micronaut-graalpy-python-package-maven-java.html
- GraalPy wheels: https://www.graalvm.org/python/wheels/
