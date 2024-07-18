FROM openjdk:21
LABEL authors="aleckbb"

COPY ./scrapper/target/scrapper.jar scrapper.jar

ENTRYPOINT ["java","-jar","/scrapper.jar"]
