Overview
----

## Technologies used in this project
 
- [Spring boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Postgres](https://www.postgresql.org/docs/)
- [Jpa](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.repositories), [hibernate](https://hibernate.org/orm/documentation/5.4/)
- [Websocket](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html)
- [Quartz](http://www.quartz-scheduler.org/documentation/)
- [Flyway](https://flywaydb.org/documentation/)
- [Jsonwebtoken(jwt)](https://jwt.io/introduction/)
- [Swagger](https://swagger.io/docs/)
- [AWS](https://docs.aws.amazon.com/)
- [Maven](https://maven.apache.org/guides/index.html)

Project download and run
----

- Pull project code
```bash
git clone https://github.com/odakota/tms-serve.git
```

- Install postgresql
```
https://www.postgresql.org/download/
```

- Init database
```
create database "tms-serve" owner postgres;
```

- Development mode operation
```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```