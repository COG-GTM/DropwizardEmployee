# Parity harness

Black-box HTTP parity check for the Java 21 migration.

- `requests.tsv` - the replayed requests (both JAX-RS methods on `/employee`, error paths, admin `/ping` and `/healthcheck`).
- `capture.sh <jar> <outdir>` - deletes the H2 file, runs `db migrate example.yml`, starts `server example.yml`, replays every request and stores status, content type and normalized body (sorted keys; `id`, `timestamp`, `duration` and error log ids masked).
- `compare.sh [baseline] [candidate]` - diffs two capture directories and exits non-zero on any mismatch.

`baseline/` was captured from the unmodified Dropwizard 1.0.5 build on JDK 8:

    JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 /usr/share/maven/bin/mvn -B clean package
    JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 parity/capture.sh target/DropwizardEmployee-1.0.5.jar parity/baseline
