docker pull rabbitmq:3-management

docker run \
 -e RABBITMQ_DEFAULT_USER=pigeoncat\
 -e RABBITMQ_DEFAULT_PASS=root\
 --name mq \
 --hostname mq1 \
 -p 15672:15672 \
 -p 5672:5672 \
 -d \
 rabbitmq:3-management





 firewall-cmd --permanent  --add-port=15672/tcp

 firewall-cmd --permanent  --add-port=5672/tcp

 firewall-cmd --reload