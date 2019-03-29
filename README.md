# ms-shipping-cart
For creating docker images and running with help of docker-compose.


-------------------
Docker Build and Run commands:
---------------------

docker build -t  ravisuhasaria/ms-shopping-cart:eureka .
docker build -t  ravisuhasaria/ms-shopping-cart:customer1  .
docker build -t  ravisuhasaria/ms-shopping-cart:item1  .
docker build -t  ravisuhasaria/ms-shopping-cart:order1  .

-------------------
To Run All:
--------------------

set RABBIT_MQ_HOST=192.168.99.100

docker network create shoppingcart_ms-network


docker run -ti -d --network shoppingcart_ms-network   --name inst-sc-zipkin -p 9411:9411 -e RABBIT_URI=amqp://192.168.99.100  -d ravisuhasaria/shopping-cart:sc-zipkin 

docker run -d --network shoppingcart_ms-network   --name sc-rabbit -p 5672:5672 -p 15672:15672 -e RABBIT_URI=amqp://192.168.99.100  ravisuhasaria/shopping-cart:rabbitmq-3-management


docker-compose up -d

----------------------------
Validation URLs:
----------------------------

Zipkin:
http://192.168.99.100:9411/zipkin/

Rabbit MQ:
http://192.168.99.100:15672/

Eureka:
http://192.168.99.100:8761

-----------------------
Rest APIs:
------------------------


GET: http://192.168.99.100:8001/shopping-cart/customers

GET: http://192.168.99.100:8002/shopping-cart/items

GET: http://192.168.99.100:8003/shopping-cart/orders


POST: http://192.168.99.100:8003/shopping-cart/order-for-existing-customer
With body as example:

{
    "custId": 1,
    "itemSku": 1,
    "ordQuantity": 1
}


POST: http://192.168.99.100:8003/shopping-cart/order-for-new-customer
With body as example:
{
    "fname": "FistName",
    "lname": "LastName",
    "itemSku": 2,
    "ordQuantity": 2
}






