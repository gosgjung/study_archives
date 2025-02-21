# Docker + Spring Boot Helloworld



# Docker Build/Run

## nginx Docker 이미지 빌드스크립트 다운로드

docker hub에서 nginx 도커 이미지 중 현재(2019.12.01) stable 버전인 1.16.1 버전을 wget으로 다운로드 받는다.  

브라우저상에서 이것 저것 입력하기 귀찮다면 아래의 커맨드를 때리자.  

```bash
$ wget https://raw.githubusercontent.com/nginxinc/docker-nginx/e3bbc1131a683dabf868268e62b9d3fbd250191b/stable/buster/Dockerfile
```



## Dockerfile 빌드

도커 이미지를 빌드하는 과정이다.  
여기서는 도커 이미지의 이름을 nginx-helloworld:0.1 

```bash
$ docker build --tag helloworld/nginx-boot:0.1 .
```

- **helloworld/**nginx-boot:0.1 
  helloworld 라는 네임스페이스를 지정했다. 이미지의 충돌을 피하기 위해서 되도록 네임스페이스를 붙이는 것을 추천하는 편이다.
- helloworld/**nginx-boot**:0.1  
  이미지 명이다.  
- helloworld/nginx-boot**:0.1**  
  태그명이다. : 과 함께 붙여서 지정한다.  

  

## docker image 조회

```bash
$ docker image ls
REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
helloworld/nginx-boot   0.1                 a0cf95f56ce4        3 minutes ago       126MB
...
```

helloworld/nginx-boot 라는 이름의 image가 보이는 것을 확인 가능하다.  

  

## docker container 실행

생성한 도커 이미지를 실행해본다.  

docker container run명령으로 실행 가능하다.  

```bash
$ docker container run -d -p 9993:80 --name nginx-boot --rm helloworld/nginx-boot:0.1 
```



- -d 옵션  

  데몬으로 실행하고자 할때 -d옵션을 붙인다.  
  컨테이너 실행시 -d 옵션을 붙이면 표준 출력(stdout)으로 해시값처럼 보이는 문자열이 출력된다.  

- -p옵션
  호스트(맥북)의 9993 포트를 컨테이너(nginx 설치된 os)의 80 포트로 연결되도록 하는 포트 포워딩 옵션이다.  

- --name 옵션  
  nginx-boot라는 이름으로 container를 실행시키겠다는 의미  

- --rm 옵션  
  docker 종료시 container를 삭제하겠다는 옵션  
  종료시 이름이 등록되어 있기 때문에 삭제하는 것이 개발 환경에서는 편하다.  



### 컨테이너의 종료

컨테이너의 종료는

```bash
$ docker container ls
CONTAINER ID        IMAGE                       COMMAND                  CREATED             STATUS              PORTS                  NAMES
677cba2c3fb0        helloworld/nginx-boot:0.1   "nginx -g 'daemon of…"   6 seconds ago       Up 5 seconds        0.0.0.0:9993->80/tcp   nginx-boot

# docker container stop [container Id]
$ docker container stop 7107f8bc4865

# 또는
# docker container stop [container name]
$ docker container stop nginx-boot
```



# Spring Boot

별다른 설정 없이 

- application.properties
  context path지정 
- Controller
  Request Mapping

을 하고 mvn package를 수행한다.



**application.properties**

```properties
server.port=80
# nginx 내에서 /hello 가 context root가 된다.
server.servlet.context-path=/hello
server.servlet.session.timeout=60
```



**HelloController.java**

```java
package com.study.deploy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping(value = "/")
    public String helloPage(){
        return "/hello";
    }
}
```



**resources/templates/hello.html**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
googogo
</body>
</html>
```



### 접속

자, 이제 Spring Boot App을 구동하고 나서  

- http://localhost:80/hello 에  

접속하면 gogogo 라는 문자열이 보일 것이다. 이제 이 spring boot app을 Nginx컨테이너로 배포해야 한다. Nginx컨테이너는 현재 개발 PC의 9993포트로 연결되어 있다는 것을 기억하고 있자.



# 컨테이너에 jar 파일 복사

  

```bash
$ docker cp deploy-0.0.1-SNAPSHOT.jar nginx-boot:/tmp
$ docker exec -it nginx-boot bash
root@8fe6e9d95177:/# cd tmp
root@8fe6e9d95177:/tmp# ls -al
total 21668
drwxrwxrwt 1 root root        4096 Dec  1 13:31 .
drwxr-xr-x 1 root root        4096 Dec  1 13:31 ..
-rw-r--r-- 1  501 dialout 22177902 Dec  1 13:13 deploy-0.0.1-SNAPSHOT.jar

root@8fe6e9d95177:/tmp# wget https://download.java.net/openjdk/jdk10/ri/openjdk-10+44_linux-x64_bin_ri.tar.gz

root@8fe6e9d95177:/tmp# mv openjdk-10+44_linux-x64_bin_ri.tar.gz /usr/local/src/

root@8fe6e9d95177:/tmp# cd /usr/local/src

root@5aff70499779:/usr/local/src# tar xvzf openjdk-10+44_linux-x64_bin_ri.tar.gz

root@5aff70499779:/usr/local/src# mv /usr/local/src/jdk-10 /usr/local/java

root@5aff70499779:/usr/local/src# vim /etc/profile
export JAVA_HOME=/usr/local/java/
export JRE_HOME=/usr/local/java/jre/
export CLASSPATH=/usr/local/java/lib/tools.jar
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin:$CLASSPATH
:wq

root@5aff70499779:/usr/local/src # source /etc/profile
root@5aff70499779:/usr/local/src # java -version
root@5aff70499779:/usr/local/src # cd /tmp
root@5aff70499779:/tmp # java -jar deploy-0.0.1-SNAPSHOT.jar &

# TODO
  0 apt-get update, install wget, vim : Dockerfile 내에 기술할 것
  1 java - alternative 관련 내용 추가
  2 service로 등록해서 백그라운드로 돌도록 하자.
  3 nginx.conf

# nginx.conf 파일이 어느곳에 존재하는지 확인
root@4c50c0a1c8d0:/# find / -name nginx.conf
root@4c50c0a1c8d0:/etc/nginx# apt-get update 
root@4c50c0a1c8d0:/etc/nginx# apt-get install vim 

root@4c50c0a1c8d0:/etc/nginx# mv nginx.conf nginx.conf.org
root@4c50c0a1c8d0:/etc/nginx# vim nginx.conf
root@5aff70499779:/usr/local/src#


```



# nginx.conf



```json

user  nginx;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    include /etc/nginx/conf.d/*.conf;

server {
    listen 80 default_server;
    listen [::]:80 default_server;
    server_name _;
    root /usr/share/nginx/html;
    
    # Load configuration files for the default server block.
    include /etc/nginx/default.d/*.conf;

    location /hello {
        proxy_pass http://localhost:9998/hello;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Powarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host $http_host;
    }
}

}

```



나머지는 내일하자... 시간이 ... ㄷㄷㄷ



