# youblogit
This project is used to explore Spring Cloud and Web Flux platforms and identify some best practices to use in production systems.
It is not itself intended for production!
Passwords should be stored hashed or encrypted in config-repo directory. 
Remember to add config-repo directory to .gitignore or better add it to a private repo.

Project uses mongo-mysql-rabbitmq-(kafka)
Before start up run:
docker run --name mongodb -d mongo -p 27017:27017
docker run --name mysql -e MYSQL_ROOT_PASSWORD=pwd -d mysql:5.7.29 -p 3306:3306
docker run -d --name some-rabbit -p 8085:15672 -p 5672:5672 rabbitmq:3-management
Then run:               Arguments                             ENV_VARS(To add them in config later)
1.Config Server         -Dspring.profiles.active=native       ENCRYPT_KEY=my-very-secure-encrypt-key, SPRING_SECURITY_USER_NAME=u, SPRING_SECURITY_USER_PASSWORD=p
2.Eureka                -Dspring.profiles.active=native       CONFIG_SERVER_PWD=p,CONFIG_SERVER_USR=u
3.Authorization Server                                        CONFIG_SERVER_USR=u, CONFIG_SERVER_PWD=p
4.Gateway                                                     CONFIG_SERVER_USR=u, CONFIG_SERVER_PWD=p
5.Posts service                                               CONFIG_SERVER_USR=u, CONFIG_SERVER_PWD=p
6.Review service                                              CONFIG_SERVER_USR=u, CONFIG_SERVER_PWD=p
7.User service                                                CONFIG_SERVER_USR=u, CONFIG_SERVER_PWD=p
8.Blog service                                                CONFIG_SERVER_USR=u, CONFIG_SERVER_PWD=p

DISCLAIMER
This SOFTWARE PRODUCT is provided by THE PROVIDER "as is" and "with all faults." THE PROVIDER makes no representations or warranties of any kind concerning the safety, suitability, lack of viruses, inaccuracies, typographical errors, or other harmful components of this SOFTWARE PRODUCT. There are inherent dangers in the use of any software, and you are solely responsible for determining whether this SOFTWARE PRODUCT is compatible with your equipment and other software installed on your equipment. You are also solely responsible for the protection of your equipment and backup of your data, and THE PROVIDER will not be liable for any damages you may suffer in connection with using, modifying, or distributing this SOFTWARE PRODUCT. 
