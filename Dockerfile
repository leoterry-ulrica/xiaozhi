FROM weifj/centos:jdk-tomcat-8
MAINTAINER weifj-web

#add war to webapps
ADD ./dcm-service/dcm-service-biz/target/dcm-service-biz.war /opt/soft/apache-tomcat-8.5.14/webapps/
ADD ./dcm-service/dcm-service-file/target/dcm-service-file.war /opt/soft/apache-tomcat-8.5.14/webapps/bdp-service-file.war
ADD ./dcm-service/dcm-service-sga/target/dcm-service-sga.war /opt/soft/apache-tomcat-8.5.14/webapps/
ADD ./dcm-webconsumer/target/dcm-webconsumer.war /opt/soft/apache-tomcat-8.5.14/webapps/bdp-webconsumer.war


# Expose ports.  
EXPOSE 2468

#define entry point which will be run first when the container starts up
#如果需要进入容器，则建议把以下命令屏蔽
ENTRYPOINT /opt/soft/apache-tomcat-8.5.14/bin/startup.sh && tail -F /opt/soft/apache-tomcat-8.5.14/logs/catalina.out