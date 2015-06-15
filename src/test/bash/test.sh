#!/bin/bash

lib=$(dirname $0)/../../main/bash/bin/lib/std.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }
lib=$(dirname $0)/../../main/bash/bin/lib/linux.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }
lib=$(dirname $0)/../../main/bash/bin/lib/ec2.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }

verbose=true

#update-nfs-server-ip-in-local-config $@
get-internal-ip-for-name $@

