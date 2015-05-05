
Individual tests:

mvn -Dmaven.surefire.debug="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005" -Dtest=Ec2DescribeInstancesParserTest test