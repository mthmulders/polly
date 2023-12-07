#!/usr/bin/env bash
set -euo pipefail

docker exec -it polly_postgresql_database psql -U postgres
