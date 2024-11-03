### run with cmd

##### Create jar
```bash 
mvn clean package -DskipTests
```
Package command is not required for docker run, because it is present in [Dockerfile](./Dockerfile), line 5

```
java -cp /path/to/wiremock-docker-1.0-SNAPSHOT.jar com.hill.WireMockExternalStubs /path/to/stubs.json
java -jar /path/to/wiremock-docker-1.0-SNAPSHOT.jar /path/to/stubs.json
```
Main class specifying `com.hill.WireMockExternalStubs` is optional because it's done in [pom file](https://github.com/Mike-first/wiremock-docker/blob/main/pom.xml#L69)
#### [stubs.json](./src/main/resources/stubs.json) example
```
[
  {
    "url": "/local/ajax/current-city.php?CODE=derbent",
    "response": {
      "status": 200,
      "contentType": "application/json",
      "body": "{\"city\":\"Дербент\",\"code\":\"derbent\",\"guid\":\"c06f5863-f1b8-4eeb-bbd4-10eb72bc4f24\"}"
    }
  },
  {
    "url": "/local/ajax/current-city.php?CODE=taganrog",
    "response": {
      "status": 200,
      "contentType": "application/json",
      "body": "{\"city\":\"Таганрог\",\"code\":\"taganrog\",\"guid\":\"0d198a8f-dbe8-4f1b-839f-9a8a49bd3e66\"}"
    }
  }
]
```
---
### run in container
##### Create image
```bash 
docker build -t wiremock-i-24 .
```
##### Create & start container
```bash 
docker run --name wiremock-24 -p 9090:8080 --detach wiremock-i-24
```
##### Start container
```bash 
docker start wiremock-24
```
##### WireMock status check
```bash 
curl http://localhost:9090/__admin/
```
##### Stop container
```bash 
docker stop wiremock-24
```
##### Remove container
```bash 
docker rm wiremock-24
```
##### Remove image
```bash 
docker rmi wiremock-i-24
```
---
