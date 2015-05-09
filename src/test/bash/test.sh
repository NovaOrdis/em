#!/bin/bash

lib=$(dirname $0)/../../main/bash/lib/std.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }
lib=$(dirname $0)/../../main/bash/lib/linux.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }

verbose=true

get-nfs-server-name ./fstab ./hosts
