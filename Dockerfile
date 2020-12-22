FROM openjdk:11-jdk-buster
ADD ./build/libs/event-search-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/event-search-0.0.1-SNAPSHOT.jar"]
EXPOSE 8181
EXPOSE 9200
EXPOSE 9201