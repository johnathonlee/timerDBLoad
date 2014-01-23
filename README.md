Author: John Lee <jolee@redhat.com>

Sample using the EJB's @Schedule for Database Load test

The sample runs every 5 seconds

to run:
export JBOSS_HOME=<path to jboss>

/mvn clean install jboss-as:deploy

/mvn jboss-as:undeploy

BMRule to force failure:
RULE 1
CLASS oracle.jdbc.driver.OracleStatement
METHOD executeQuery
IF TRUE
DO debug("-----------------------------------------> INTRODUCING ERROR");
   throw new java.sql.SQLException("breakme", "SQLState", 28);
ENDRULE
