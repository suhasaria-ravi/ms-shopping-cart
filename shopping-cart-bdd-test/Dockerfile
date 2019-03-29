FROM openjdk:8-jdk-alpine
WORKDIR /
ADD target/shopping-cart-bdd-test-0.0.1-SNAPSHOT.jar //
EXPOSE 8005
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom  -Dspring.profiles.active=dock  -jar /shopping-cart-bdd-test-0.0.1-SNAPSHOT.jar" ]