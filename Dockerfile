FROM ubuntu:latest
LABEL authors="pavit"

ENTRYPOINT ["top", "-b"]