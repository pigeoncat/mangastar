```
docker run --name comicstar-app -p 8899:8899 -d  comicstar-app:1.0

docker run --name comicstar-app -p 8899:8899 -e JAVA_OPTS="-Xmx1g" -d comicstar-app:1.0


docker run --name comicmq-app -p 7777:7777 -d  comicmq-app:1.0

docker run --name comicmq-app -p 7777:7777 -e JAVA_OPTS="-Xmx256m" -d comicmq-app:1.0


docker run --name comicspider-app -p 9999:9999 -d  comicspider-app:1.0

docker run --name comicspider-app -p 9999:9999 -e JAVA_OPTS="-Xmx256m" -d comicspider-app:1.0
```



```
docker run \
--name comicstar-web -d \
-p 80:80 \
--restart=always \
--privileged=true \
-v /root/comicstar-front/web/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /root/comicstar-front/web/html:/etc/nginx/html \
-v /root/comicstar-front/web/log:/var/log/nginx \
nginx
```

自制一个 java17_chinese 的镜像，内置java17环境，并且安装中文字符集，解决中文编码问题。去网上找一下教程。

华为云和阿里云买两个服务器，2核4GB

前端采用宝塔nginx部署

开发完成后用maven打成jar包，制成docker容器运行。

目录下提供一个样板，照着样板换一下即可。

