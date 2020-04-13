FROM openjdk:7
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
ENV DISPLAY host.docker.internal:0
RUN javac Docker.java
CMD ["java", "Docker"]
