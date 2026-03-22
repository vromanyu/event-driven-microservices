[![build-all-workflow](https://github.com/vromanyu/event-driven-microservices/actions/workflows/build-all.yml/badge.svg?event=workflow_dispatch)](https://github.com/vromanyu/event-driven-microservices/actions/workflows/build-all.yml)

# Event-Driven Microservices

A hands-on playground for experimenting with **event-driven communication** using **Apache Kafka**.

## Overview

This repository explores **event-driven microservices** patterns. It demonstrates how microservices can collaborate asynchronously using events, without tight coupling.

<details>
<summary>Orchestration</summary>
<br/>

  ```bash
  cd choreography/docker
  docker compose up -d
  ```
**NGINX** was added to handle **SSL** termination in front of the gateway!
</details>
<details>
<summary>CQRS</summary>
<br/>
  
  ```bash
  cd cqrs/docker
  ISSUER_URI=<issuerUri> docker compose up -d
  ```

Some additional features were added:
- Use **Redis** in production and **ConcurrentHashmap** in development to cache query-ms responses.
- Use Rate Limiting using **Bucket4j**. Redis is used in production, while Caffeine is used in development/testing.
- Protect the gateway and downstream services using **OAuth2 Resource Server**.
- **NGINX** handles **SSL** termination in front of the gateway.
- The `ISSUER_URI` variable must be provided in Docker Compose.

</details>

<details>
<summary>SAGA</summary>
<br/>
SAGA Orchestration pattern is on the works!
</details>

