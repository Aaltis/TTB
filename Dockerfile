##build stage
FROM openjdk:8 as builder
WORKDIR TTB
#add content of directory where this file is to container instanse workdir tmp
ADD . .
#this is how you run gradle.
RUN ["./gradlew", "clean", "build", "-xtest", "--stacktrace"]
#check with:
#docker run --rm -it TTB ls -al //tmp/build/libs

FROM openjdk:8-jdk-alpine
VOLUME /main-app
COPY --from=builder //TTB/build/libs/*.jar TTB.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/TTB.jar"]