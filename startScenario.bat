echo "Please verify that JAVA_HOME points to a JRockit JDK"
set JAVA_HOME
pause
mvn exec:java -Dexec.mainClass="be.steria.datapoc.IntegrationTests.tools.StartScenario" -Dexec.args="%1 %2 %3"