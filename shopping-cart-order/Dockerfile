FROM openjdk:8-jdk-alpine
WORKDIR /
ADD target/shopping-cart-order-0.0.1-SNAPSHOT.jar //
EXPOSE 8003

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom  -Dspring.profiles.active=docker  -jar /shopping-cart-order-0.0.1-SNAPSHOT.jar" ]