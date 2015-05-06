#!/bin/bash

[ -f $(dirname $0)/em.shlib ] && . $(dirname $0)/em.shlib || { echo "$(dirname $0)/em.shlib not found" 1>&2; exit 1; }
[ -f $(dirname $0)/amq.shlib ] && . $(dirname $0)/amq.shlib || { echo "$(dirname $0)/amq.shlib not found" 1>&2; exit 1; }

debug_off=false

configureTransportConnectors /Users/ovidiu/tmp/jboss-a-mq-6.1.0.redhat-379/etc/activemq.xml ~/tmp b01 5555 7777 "b01 b02"
configureNetworkConnectors /Users/ovidiu/tmp/jboss-a-mq-6.1.0.redhat-379/etc/activemq.xml ~/tmp b01 7777 "b01 b02" alice alice123