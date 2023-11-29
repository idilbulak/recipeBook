FROM openjdk:17-ea-jdk-buster

# maven installation
RUN apt-get -y update && \
    apt-get -y install wget && \
    wget https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && \
    tar xzvf apache-maven-3.6.3-bin.tar.gz && \
    mv apache-maven-3.6.3 /usr/local/apache-maven && \
    ln -s /usr/local/apache-maven/bin/mvn /usr/bin/mvn

COPY . /app
WORKDIR /app
RUN mvn clean package

EXPOSE 8080

ENTRYPOINT ["java","-jar","target/recipeBook-0.0.1-SNAPSHOT.jar"]
