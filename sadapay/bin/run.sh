#!/usr/bin/env bash

# Add script to run program here.
# Complete bin/setup so that after it is
# run, bin/run can be used to launch
# it.

# This variable contains absolute path of this `run` script
WORKING_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"/../solution
cd $WORKING_DIR

# This assumes the code to be in the folder <project_root>/solution, which is
# used as working directory
java -cp ./build/libs/Assignment-1.0-SNAPSHOT.jar com.sadapay.assignment.Supermarket $@