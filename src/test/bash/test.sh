#!/bin/bash

lib=$(dirname $0)/../../main/bash/bin/lib/std.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }
lib=$(dirname $0)/../../main/bash/bin/lib/linux.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }
lib=$(dirname $0)/../../main/bash/bin/lib/ec2.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }
lib=$(dirname $0)/../../main/bash/bin/overlays/lib/eap.shlib; [ -f ${lib} ] && . ${lib} || { echo "${lib} not found" 1>&2; exit 1; }

verbose=true

out=$(get-element-names-corresponding-to-attribute "XXX" "a:AAA,b:XXX,c:CCC,d:XXX,e:MMM,f:XXX,b:XXX")
echo ">${out}<"

