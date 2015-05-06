#!/bin/bash

lib_dir=$(dirname $0)/../../main/bash/lib/

[ -f ${lib_dir}/em.shlib ] && . ${lib_dir}/em.shlib || { echo "${lib_dir}/em.shlib not found" 1>&2; exit 1; }
[ -f ${lib_dir}/linux.shlib ] && . ${lib_dir}/linux.shlib || { echo "${lib_dir}/linux.shlib not found" 1>&2; exit 1; }

verbose=true

update-etc-hosts $@