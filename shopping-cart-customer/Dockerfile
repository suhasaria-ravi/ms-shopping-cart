FROM openjdk:8-jdk-alpine
WORKDIR /
ADD target/shopping-cart-customer-0.0.1-SNAPSHOT.jar //
EXPOSE 8001
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom  -Dspring.profiles.active=docker  -jar /shopping-cart-customer-0.0.1-SNAPSHOT.jar" ]