#!/usr/bin/env bash
# Usage: parity/capture.sh <jar> <outdir>   (JAVA_HOME selects the JDK)
# Seeds a fresh H2 DB via `db migrate`, starts the server with example.yml,
# replays parity/requests.tsv and writes one normalized file per request.
set -euo pipefail
cd "$(dirname "$0")/.."
JAR="$1"; OUT="$2"
JAVA="${JAVA_HOME:+$JAVA_HOME/bin/}java"
LOGS=target/parity-logs
mkdir -p "$OUT" "$LOGS"; rm -f "$OUT"/*.txt
rm -f target/example.*.db target/example.mv.db target/example.trace.db target/example.h2.db
"$JAVA" -jar "$JAR" db migrate example.yml > "$LOGS/migrate-$(basename "$OUT").log" 2>&1
"$JAVA" -jar "$JAR" server example.yml > "$LOGS/server-$(basename "$OUT").log" 2>&1 &
PID=$!
trap 'kill $PID 2>/dev/null || true; wait $PID 2>/dev/null || true' EXIT
for _ in $(seq 1 60); do curl -sf http://localhost:8081/ping >/dev/null && break; sleep 1; done
"$JAVA" -version 2>&1 | head -1 > "$OUT/jdk.info"
grep -v '^#' parity/requests.tsv | while IFS=$'\t' read -r id port method path ctype accept body; do
  args=(-s -o /tmp/parity-body -w '%{http_code}\t%{content_type}' -X "$method" -H "Accept: $accept")
  [ "$ctype" != "-" ] && args+=(-H "Content-Type: $ctype")
  if [ "$body" != "-" ]; then args+=(--data-binary "$body"); elif [ "$method" = POST ]; then args+=(--data-binary ""); fi
  meta=$(curl "${args[@]}" "http://localhost:$port$path")
  {
    echo "request: $method $path"
    echo "status: $(cut -f1 <<<"$meta")"
    echo "content-type: $(cut -f2 <<<"$meta" | tr 'A-Z' 'a-z' | tr -d ' ')"
    echo "body:"
    python3 parity/normalize.py < /tmp/parity-body
  } > "$OUT/$id.txt"
done
echo "captured $(ls "$OUT"/*.txt | wc -l) requests into $OUT"
