# Java Client/Server Project

## Overview

This project consists of a client program that monitors a directory for new Java properties files and a server program that receives filtered properties data from clients. The server reconstructs the properties files and writes them to disk.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.6 or higher

## Building the Project

This project is organized as a multi-module Maven project. To build both the client and server modules, navigate to the root directory of the project and run:

```bash
mvn clean install

## Running the Client
```bash
java -cp client/target/client-1.0-SNAPSHOT.jar com.example.client.Client /path/to/client-config.properties

## Running the server
```bash
java -cp server/target/server-1.0-SNAPSHOT.jar com.example.server.Server /path/to/server-config.properties

