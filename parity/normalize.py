#!/usr/bin/env python3
"""Normalize a captured HTTP body: sort JSON keys, mask volatile fields."""
import json
import re
import sys

VOLATILE = {"id", "timestamp", "duration"}


def mask(value):
    if isinstance(value, str):
        return re.sub(r"\(ID [0-9a-f]+\)", "(ID <masked>)", value)
    if isinstance(value, dict):
        return {k: ("<masked>" if k in VOLATILE else mask(v)) for k, v in value.items()}
    if isinstance(value, list):
        return [mask(v) for v in value]
    return value


raw = sys.stdin.read()
try:
    print(json.dumps(mask(json.loads(raw)), sort_keys=True, indent=2))
except ValueError:
    text = re.sub(r"\s+", " ", raw).strip()
    print(text)
