#!/bin/bash

DOCKERFILE_PATH=./
IMAGE_NAME=ct-config
IMAGE_TAG=1.0

run() {
	name=$1
	sudo docker run -itd -p 8001:8001 --network host -v /etc/localtime:/etc/localtime --name=${IMAGE_NAME}-${name} ${IMAGE_NAME}:${IMAGE_TAG} /bin/bash
}

del() {
	name=$1
	sudo docker rm -f ${IMAGE_NAME}-${name}
}

docker_name="test-01"
del $docker_name
run $docker_name

# win
# docker build --network=host --no-cache -t ct-config:0.1 ./
# docker rm -f ct-config
# docker run -itd -p 8001:8001 --name=ct-config ct-config:0.1 /bin/bash
# docker push ct-config:0.1

# docker logs --tail=100 -f ct-config
# docker exec -it ct-config bash