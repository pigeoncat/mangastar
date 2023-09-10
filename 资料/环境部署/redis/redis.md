拉取redis7镜像

```
docker pull redis:7.0.5
```

创建redis容器的挂载目录

```
mkdir -p /root/data/redis7/conf
mkdir -p /root/data/redis7/data
```

docker redis 7  默认没带redis.conf

https://github.com/redis/redis/tags

去github下载tar包取出redis.conf,修改下配置

```
bind 0.0.0.0                # 修改这部分，使redis可以外部访问
protected-mode yes          # 保护模式，默认yes，如果不需要保护模式可以设置为no
port 6379                   # 端口号， 默认是6379，看个人情况修改
logfile /data/redis.log     # 日志文件存放位置
daemonize no                # 用守护线程的方式启动，关闭
dir /data                   # 数据存放目录
requirepass <your password> # 密码
appendonly yes              # redis 开启AOF方式持久化 默认是no
appenddirname "aof"         # aof文件存放的文件夹名称，不能带/,根据个人情况决定是否修改
```

上传配置文件到 redis 配置挂载目录  /root/data/redis7/conf

启动 redis

```
docker run -p 6379:6379 \
--privileged=true \
-v /root/data/redis7/data:/data \
-v /root/data/redis7/conf:/usr/local/etc/redis \
--name redis7 \
--restart=always \
-d redis:7.0.5 \
redis-server /usr/local/etc/redis/redis.conf
```

链接客户端测试

```
docker exec -it redis7 redis-cli
auth  <password>
```





































