FROM java:8
WORKDIR /data
# VOLUME 指定了临时文件目录为/tmp
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /askanswer
# 将jar包添加到容器中并更名为app.jar
COPY *.jar app.jar
# 运行jar包
RUN bash -c 'touch /app.jar'
ENTRYPOINT java -jar /data/jar/app.jar --server.port=8082 --spring.profiles.active=dev
EXPOSE 8082