#!/usr/bin/env bash
set -euo pipefail

rm -Rf src/main/asciidoc/img/structurizr-*
structurizr-cli export -o src/main/asciidoc/img -w src/main/structurizr/workspace.dsl -f plantuml
