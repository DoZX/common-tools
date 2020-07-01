#!/bin/bash

DOCKERFILE_PATH=./
IMAGE_NAME=ct-server
IMAGE_TAG=1.0

run() {
	name=$1
	sudo docker run -itd -p 9200:9200 -p 9300:9300 -p 5601:5601 --name=ct-server-${name} ${IMAGE_NAME}:${IMAGE_TAG} /bin/bash
}

del() {
	name=$1
	sudo docker rm -f ct-server-${name}
}

docker_name="test-01"
del $docker_name
run $docker_name

# win
# docker build --network=host --no-cache -t ct-server:0.1 ./
# docker rm -f ct-server
# docker run -itd -p 9200:9200 -p 9300:9300 -p 5601:5601 --name=ct-server ct-server:0.1 /bin/bash

# docker push ct-server:0.1