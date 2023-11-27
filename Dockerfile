# pull JDK image
FROM eclipse-temurin:11

# set up a working directory
WORKDIR /opt/app

# copy Java files into the container
COPY App.java .

# compile it
RUN javac App.java

# runs the compiled application
CMD ["java", "App"]
