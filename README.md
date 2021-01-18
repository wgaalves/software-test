#  Software Engineer Take-home assignment

## Tools
* java 11
* Docker
* spring framework

## Usage 

### building
```
gradle assemble
docker build -t software-test .
docker run -d -p 80:8080 software test
```

### Running docker image from DockerHub

```
docker run -d -p 80:8080 willalvesbr/software-test
```
