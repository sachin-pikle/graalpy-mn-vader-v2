# Demo: Sentiment Analysis with VADER, GraalPy and Micronaut

<small>version: v2</small>

This repo is a small Micronaut sample that accepts an uploaded text file, sends the decoded text from Java into GraalPy, runs VADER sentiment scoring, and shows the result in a simple browser UI.

The sample uses Micronaut's injected `@GraalPyModule` pattern rather than manually creating a polyglot `Context`.

## Current Sample

- Micronaut 5.0.0-SNAPSHOT
- Java 25 bytecode
- Preferred local runtime: `sdk use java 25.0.2-graal`
- GraalPy build tooling via `graalpy-maven-plugin` 25.0.2
- Python dependency pinned to `vaderSentiment==3.3.2`
- One static page with upload, preview, sentiment card, raw JSON, and a clear button

## Demo Flow

1. Upload a text-based review file from the browser.
2. Micronaut receives the multipart upload at `/api/reviews/analyze`.
3. Java decodes the upload bytes to UTF-8 text.
4. `GraalPySentimentService` calls the injected `SentimentModule`.
5. GraalPy runs `sentiment_app.py` and returns a JSON string.
6. Java deserializes that JSON into `ReviewAnalysisView`.
7. The UI shows the review text, sentiment label, compound score, raw JSON, and an emoji.
8. Clear resets both the file input and the visible output.

## Key Files

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

## Run Locally

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

## Executable Jar

```bash
sdk use java 25.0.2-graal
```

```bash
./mvnw package
```

```bash
java -jar target/graalpy-mn-vader-v2-0.1.jar
```

## Inspect The Jar In JD-GUI

```bash
java -jar jd-gui-1.6.6.jar
```

When JD-GUI opens, open `target/graalpy-mn-vader-v2-0.1.jar`.

Look for these paths inside the jar:

- `org.graalvm.python.vfs/src/sentiment_app.py`
- `org.graalvm.python.vfs/venv/`

See the bundled Python module, embedded GraalPy virtual filesystem, and installed Python packages packaged into the executable jar.

## Native Image

```bash
sdk use java 25.0.2-graal
```

```bash
./mvnw package -Dpackaging=native-image
```

```bash
./target/graalpy-mn-vader-v2
```

## Sample Inputs

- `samples/product-coffee-grinder-positive.txt`
- `samples/product-robot-vacuum-negative.txt`

## Main Differences From v1

- `v2` uses `io.micronaut.graal-languages:micronaut-graalpy` and an injected `@GraalPyModule` interface; `v1` uses direct GraalPy embedding with `GraalPyResources` plus explicit `org.graalvm.python:python` and `org.graalvm.python:python-embedding`.
- `v2` now runs on Micronaut 5.0.0-SNAPSHOT, Java 25 bytecode, `sdk use java 25.0.2-graal`, and GraalPy 25.0.2.
- `v2` keeps the Python module under `src/main/resources/org.graalvm.python.vfs/src/sentiment_app.py`; `v1` keeps it at `src/main/resources/python/sentiment_app.py`.
- `v2` calls the Python function through the injected `SentimentModule` and keeps `src/main/resources/META-INF/native-image/proxy-config.json` aligned with that interface; `v1` manually evaluates the script, reads the function from Python bindings, and does not need proxy metadata.

## Notes

- The Python module lives under the GraalPy virtual filesystem path at `src/main/resources/org.graalvm.python.vfs/src`.
- The executable jar includes the GraalPy virtual filesystem and installed Python packages under `org.graalvm.python.vfs/`.
- Native-image support depends on keeping `src/main/resources/META-INF/native-image/proxy-config.json` aligned with `SentimentModule`.
- The tests cover both the service-level sentiment flow and the multipart upload endpoint.
