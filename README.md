# EasyRent API

Before starting this project, install redis, and kafka.

Update the env.list file with the appropriate redis password, and ports.

These commands assume that redis is running locally on port 6379 and 9092 respectively.

To start this project, run the following commands:

`docker build . -t easyrent-api`

`docker run --env-file ./env.list easyrent-api`