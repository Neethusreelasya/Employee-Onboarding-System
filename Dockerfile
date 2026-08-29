FROM tomcat:10.1-jdk21

WORKDIR /app
COPY . .

RUN mkdir -p web/WEB-INF/classes && \
    javac -cp "$CATALINA_HOME/lib/*:web/WEB-INF/lib/jakarta.mail-2.0.1.jar:web/WEB-INF/lib/jakarta.activation-2.0.1.jar" \
    -d web/WEB-INF/classes $(find src -name "*.java")

RUN rm -rf $CATALINA_HOME/webapps/ROOT
RUN mkdir -p $CATALINA_HOME/webapps/ROOT
RUN cp -r web/* $CATALINA_HOME/webapps/ROOT/

EXPOSE 8080
CMD ["catalina.sh", "run"]