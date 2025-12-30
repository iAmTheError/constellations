#!/usr/bin/env bash
set -euo pipefail

base="https://mast.stsci.edu/vo-tap/api/v0.1/gaiadr3"
adql="SELECT TOP 10 ra, dec, phot_g_mean_mag FROM gaia_source ORDER BY phot_g_mean_mag ASC"

curl -sS -X POST "$base/sync" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data-urlencode "REQUEST=doQuery" \
  --data-urlencode "LANG=ADQL" \
  --data-urlencode "FORMAT=csv" \
  --data-urlencode "QUERY=$adql"
