#!/usr/bin/env bash
set -euo pipefail

mvn test-compile org.pitest:pitest-maven:mutationCoverage -pl \!app,\!test,\!docs