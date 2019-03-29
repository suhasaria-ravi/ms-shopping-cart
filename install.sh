#!/bin/bash
echo 'Creating Volume Mount for Docker...'
mkdir -p ${HOME}/jarloc_sc

#echo 'getting config from git...'
#if [[ ! -d "${HOME}/jarloc_sc/microservice-docker-cart-config" ]]; then
#git clone git@github.com:hiteshjoshi1/microservice-docker-cart-config.git ${HOME}/jarloc_sc/#microservice-docker-cart-config
#fi

#echo 'Building the entire project ...'
#mvn clean package -DskipTests


echo 'Copying .....'

cp shopping-cart-customer/target/shopping-cart-customer-0.0.1-SNAPSHOT.jar     ${HOME}/jarloc_sc
cp shopping-cart-item/target/shopping-cart-item-0.0.1-SNAPSHOT.jar     ${HOME}/jarloc_sc
cp shopping-cart-netflix-eureka/target/shopping-cart-netflix-eureka-0.0.1-SNAPSHOT.jar     ${HOME}/jarloc_sc
cp shopping-cart-order/target/shopping-cart-order-0.0.1-SNAPSHOT.jar     ${HOME}/jarloc_sc
cp zipkin/target/zipkin.jar ${HOME}/jarloc_sc
echo 'All engines up, ready to launch to hyperspace .....'
docker-compose up