[![build-all-workflow](https://github.com/vromanyu/event-driven-microservices/actions/workflows/build-all.yml/badge.svg?event=workflow_dispatch)](https://github.com/vromanyu/event-driven-microservices/actions/workflows/build-all.yml)

# Event-Driven Microservices

A hands-on playground for experimenting with **event-driven communication** using **Apache Kafka**.

## Overview

This repository explores **event-driven microservices** patterns. It demonstrates how microservices can collaborate asynchronously using events, without tight coupling.

The project now includes a Docker Compose setup that allows you to easily run and experiment with the SAGA Choreography pattern locally, providing a ready-to-use environment for testing interactions between services!

```bash
cd choreography/docker
docker compose up -d
```
The CQRS pattern has been implemented! Here's the Docker Compose command:

```bash
cd cqrs/docker
docker compose up -d
```

SAGA Orchestration pattern is on the works!
