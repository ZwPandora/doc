
### docker 拉取镜像 
docker pull rabbitmq:management
### 运行镜像
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:management
第二次 
docker start rabbitmq
### 访问管理界面
http://192.168.17.128:15672
用户名密码: guest/guest

### 启动docker
systemctl start docker