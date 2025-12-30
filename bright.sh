#!/usr/bin/env bash
set -euo pipefail

base="https://mast.stsci.edu/vo-tap/api/v0.1/gaiadr3"
adql="SELECT source_id, ra, dec, phot_g_mean_mag
      FROM gaia_source
      WHERE phot_g_mean_mag <= 6
      ORDER BY phot_g_mean_mag ASC"

curl -sS -X POST "$base/sync" \
  -H "Accept: text/csv" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data-urlencode "REQUEST=doQuery" \
  --data-urlencode "LANG=ADQL" \
  --data-urlencode "FORMAT=csv" \
  --data-urlencode "QUERY=$adql" \
  > bright_stars.csv

echo "Saved $(wc -l < bright_stars.csv) lines to bright_stars.csv"
