#!/usr/bin/env bash

# Add script to:
# * Install dependencies
# * Build/Compile
# * Run Test Suit to validate
# This should generate the jar file needed to run the application.

WORKING_DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"/../solution
cd $WORKING_DIR

# This assumes the code to be in the folder <project_root>/solution, which is
# used as working directory

gradle clean build -i