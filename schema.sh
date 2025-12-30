#!/usr/bin/env bash
set -euo pipefail

base="https://mast.stsci.edu/vo-tap/api/v0.1/gaiadr3"
adql="SELECT column_name, datatype, unit, description
      FROM TAP_SCHEMA.columns
      WHERE table_name = 'gaia_source'
      ORDER BY column_name"

curl -sS -X POST "$base/sync" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data-urlencode "REQUEST=doQuery" \
  --data-urlencode "LANG=ADQL" \
  --data-urlencode "FORMAT=csv" \
  --data-urlencode "QUERY=$adql"
