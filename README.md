# Data Storage API

Spring Boot Microservices for a File Storage System.
This Service provides APIs to add, update or read Data from either a CSV or XML Data File.

## Installation Guide

This project has been written to be tested with minimal setup.
Docker is required to be running in your system to run the docker-compose file. Get Docker for Windows [here](https://docs.docker.com/docker-for-windows/install/)

There are three components to this service:

1. DocumentService
2. StorageService
3. Kafka Message Broker

The docker-compose.yml is configured to setup one Zookeeper, one Kafka broker and the Schema Registry.
Then it runs maven install on the DocumentService and StorageService microservices and brings them up.

Use the below command to bring the services up.

```
docker-compose up -d
```

Once ready you should see the below containers running.

```
$ docker ps
CONTAINER ID        IMAGE                                    COMMAND                  CREATED             STATUS              PORTS                                        NAMES
c76c2e5d20de        data-storage-service_documentservice     "java -jar -Dspring.…"   23 hours ago        Up 4 seconds        0.0.0.0:8081->8081/tcp                       data-storage-service_documentservice_1
7f4738a83bbf        data-storage-service_storageservice      "java -jar -Dspring.…"   23 hours ago        Up 5 seconds        0.0.0.0:8082->8082/tcp                       data-storage-service_storageservice_1
0db6a7a3b6db        confluentinc/cp-schema-registry:latest   "/etc/confluent/dock…"   23 hours ago        Up 34 seconds       0.0.0.0:8083->8081/tcp                       data-storage-service_schema-registry_1
24028f9805a7        confluentinc/cp-kafka:latest             "/etc/confluent/dock…"   23 hours ago        Up 34 seconds       0.0.0.0:9092->9092/tcp                       data-storage-service_kafka_1
341a195477e7        confluentinc/cp-zookeeper:latest         "/etc/confluent/dock…"   23 hours ago        Up 34 seconds       2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp   data-storage-service_zookeeper_1
```

### Optional Configuration:

The XML and CSV Data file will be stored by default at the **./file-storage/** directory of the source code.
This can be modified by updating the FILE_STORAGE_PATH variable in the .env file at the root of the source code.

```
FILE_STORAGE_PATH=./file-storage/
```

Additionally the AES key used to encrypt and decrypt the data being transferred between the DocumentService and StorageService can be modified by updating the AES_SECRET variable in the .env file.

```
AES_SECRET=testsecret
```

## Testing

There are three APIs being exposed by the DocumentService on port 8081.

1. AddData      -  PUT  localhost:8081/data
2. UpdateData   -  POST localhost:8081/data
3. GetData      -  GET  localhost:8081/data

Import the postman collection **DataStorage_APIs.postman_collection.json** present at **data-storage-service/postman-api** of the source code to test these APIs.
Or use the below sample cURL commands:

AddData:

```
curl --location --request PUT 'http://localhost:8081/data' \
--header 'Content-Type: application/json' \
--header 'fileType: CSV' \
--data-raw '[
  {
    "name": "User1",
    "dob": "1993-10-10",
    "salary": "10000"
  },
  {
    "name": "User2",
    "dob": "1993-10-10",
    "salary": "20000"
  }
]'
```


UpdateData:

```
curl --location --request POST 'http://localhost:8081/data' \
--header 'Content-Type: application/json' \
--header 'fileType: XML' \
--data-raw '[
    {
        "id": "1",
        "name": "User1",
        "dob": "1993-10-10",
        "salary": "10000"
    },
    {
        "id": "2",
        "name": "User2",
        "dob": "1993-10-10",
        "salary": "20000"
    }
]'
```

GetData:

```
curl --location --request GET 'http://localhost:8081/data' \
--header 'Content-Type: application/json'
```
