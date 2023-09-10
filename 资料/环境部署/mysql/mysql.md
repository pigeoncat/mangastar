拉取镜像

```
docker pull mysql:8.0
```

数据卷

```
mkdir -p /root/data/mysql8/conf
mkdir -p /root/data/mysql8/data
mkdir -p /root/data/mysql8/log
```

创建配置文件    /root/data/mysql8/conf/my.cnf

设置编码以支持中文

```
[mysqld]
init-connect="SET collation_connection=utf8mb4_0900_ai_ci"
init_connect="SET NAMES utf8mb4"
skip-character-set-client-handshake
```



启动容器

```
docker run -p 3306:3306  \
--name mysql8 \
--privileged=true \
-v /root/data/mysql8/log:/var/log/mysql \
-v /root/data/mysql8/data:/var/lib/mysql \
-v /root/data/mysql8/conf:/etc/mysql/conf.d \
--restart=always \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:8.0
```

设置mysql远程连接

进入容器修改密码和权限

```
docker exec -it mysql8 /bin/bash
mysql -uroot -proot
use mysql
ALTER USER 'root'@'%' IDENTIFIED BY '新密码';

# Mysql8.0 默认采用 caching-sha2-password 加密，有可能旧的客户端不支持，可改为 
# mysql_native_password; 

CREATE USER 'root'@'%' IDENTIFIED WITH MYSQL_NATIVE_PASSWORD BY '222333';

创建用户,以后用来远程连接
create user 'pigeoncat'@'%' identified by '密码';
授权
grant all on *.* to 'pigeoncat'@'%' with grant option;

刷新权限
FLUSH PRIVILEGES;
用navicat测试
```



































