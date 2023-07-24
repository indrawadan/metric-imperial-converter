# Metric to Imperial Conversion
Metric to Imperial Conversion is a Spring Boot web application that provides an API to convert metric units to imperial units and vice versa. It uses a PostgreSQL database with JDBC Template for storing conversion rules.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Usage](#usage)

## Features

- Add new conversion rules to the database (e.g., meters to feet, kilograms to pounds).
- Convert values from metric to imperial and vice versa based on the stored conversion rules.

## Getting Started

Follow these steps to set up and run the Metric to Imperial Conversion application:

1. Clone this repository to your local machine.
2. Build the application using Maven: `mvn clean install`
3. Run the application: `java -jar target/metric-imperial-conversion-1.0.0.jar`

Alternatively, you can build and run the application using docker and docker-compose

## Prerequisites

Before running the application, ensure you have the following:

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Docker

## API Endpoints
The application exposes the following API endpoints:

- `POST /api/conversion/add`: Add a new conversion rule to the database.
- `POST /api/conversion/convert`: Convert a value from one unit to another.

## Usage
To know API payload and usage, refer MetricToImperial.postman_collection.json.
