#!/usr/bin/env bash

# Add script to:
# * Remove compiled binaries and lib files
#
# This should remove all the binaries and leave only the source files

WORKING_DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"/../solution
cd $WORKING_DIR

# This assumes the code to be in the folder <project_root>/solution, which is
# used as working directory

gradle clean