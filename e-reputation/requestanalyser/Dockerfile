FROM openjdk:8
EXPOSE 8085
ADD target/project-analyser.jar project-analyser.jar
ENTRYPOINT ["java", "-jar", "project-analyser.jar"]
