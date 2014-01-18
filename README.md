Author: John Lee <jolee@redhat.com>

Sample using the EJB's @Schedule for Database Load test

The sample runs every 5 seconds

to run:
export JBOSS_HOME=<path to jboss>

/mvn clean install jboss-as:deploy

/mvn jboss-as:undeploy
