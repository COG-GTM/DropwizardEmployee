#!/usr/bin/env bash
# Usage: parity/compare.sh [baseline_dir] [candidate_dir]
cd "$(dirname "$0")/.."
BASE="${1:-parity/baseline}"; NEW="${2:-parity/migrated}"
total=0; bad=0
for f in "$BASE"/*.txt; do
  total=$((total+1)); n="$NEW/$(basename "$f")"
  if ! diff -u "$f" "$n"; then bad=$((bad+1)); fi
done
echo "compared $total endpoints/requests, $bad mismatches"
[ "$bad" -eq 0 ]
