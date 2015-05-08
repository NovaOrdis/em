#!/bin/bash

lib=$(dirname $0)/../../main/bash/lib/std.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }
lib=$(dirname $0)/../../main/bash/lib/ec2.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }

verbose=true
to-ids $@
