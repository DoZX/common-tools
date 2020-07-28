#!/bin/bash

DOCKERFILE_PATH=./
IMAGE_NAME=ct-elasticsearch
IMAGE_TAG=1.0

run() {
	name=$1
	sudo docker run -itd -p 9200:9200 -p 9300:9300 -v /etc/localtime:/etc/localtime --name=${IMAGE_NAME}-${name} ${IMAGE_NAME}:${IMAGE_TAG} /bin/bash
}

del() {
	name=$1
	sudo docker rm -f ${IMAGE_NAME}-${name}
}

docker_name="test-01"
del $docker_name
run $docker_name

# win
# docker build --network=host --no-cache -t ct-elasticsearch:0.1 ./
# docker rm -f ct-elasticsearch
# docker run -itd -p 9200:9200 -p 9300:9300 --name=ct-elasticsearch ct-elasticsearch:0.1 /bin/bash
# docker push ct-elasticsearch:0.1

# docker logs --tail=100 -f ct-elasticsearch
# docker exec -it ct-elasticsearch bash