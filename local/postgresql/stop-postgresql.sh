#!/usr/bin/env bash
set -euo pipefail

docker stop polly_postgresql_database
docker rm polly_postgresql_database