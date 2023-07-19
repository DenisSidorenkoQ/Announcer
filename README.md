# announcer


TODO: У тебя создано 2 сервиса в одном гит репо. Это называется монорепа. Тогда имело бы смысл добавить агрегационный Maven Project. Глянь что это такое и примени к проекту.

Ещё разбираюсь с его упаковкой в докер

TODO: Опиши, пожалуйста, команды сборки докер артефактов в README.MD.

FROM maven:3.6.0-jdk-11-slim AS builder
WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM adoptopenjdk/openjdk11
WORKDIR /app
COPY --from=builder /app/target/mail-service-1.0-SNAPSHOT.jar /usr/local/lib/mail-service.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/mail-service.jar"]

1. В первой строке в качестве базового образа указывается Maven 3.6.0 с JDK 11 slim-версии. 
2. Команда WORKDIR устанавливает рабочий каталог внутри контейнера в "/app". 
3. Команда COPY копирует из каталога "src" в каталог "/app/src" внутри контейнера. 
4. Команда COPY также копирует файл "pom.xml" в каталог "/app" внутри контейнера. 
5. Команда RUN выполняет команду Maven "mvn clean package" с указанным путем к файлу pom.xml для сборки java-приложения внутри контейнера.

6. Вторая часть кода начинается с команды FROM, в которой в качестве базового образа указывается AdoptOpenJDK 11.
7. Команда WORKDIR устанавливает рабочий каталог внутри контейнера на "/app". 
8. Команда COPY копирует собранный JAR-файл с предыдущего этапа (builder) по пути "/usr/local/lib/mail-service.jar" внутри контейнера. 
9. Команда EXPOSE открывает порт 8081, чтобы к нему можно было обращаться извне контейнера.
10. Команда ENTRYPOINT задает команду, которая должна выполняться при запуске контейнера. В данном случае он запускает java-приложение.