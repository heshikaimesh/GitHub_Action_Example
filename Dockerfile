FROM openjdk:11
EXPOSE 8080
ADD target/github-action-workflow.jar github-action-workflow.jar
ENTRYPOINT ["java", "-jar","/github-action-workflow.jar.jar"]