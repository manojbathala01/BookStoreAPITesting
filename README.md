# REST Assured API Automation Framework

## Overview
This is a modular and scalable API test automation framework built with:
- Java
- REST Assured
- TestNG
- Allure Reports

The framework supports:
- Environment-based configuration (`dev`, `qa`, `prod`)
- Positive and negative test separation
- Service-based logic segregation

---

## Project Structure

```
restassured-api-framework/
├── src/
│   ├── main/
│   │   └── java/utils/
│   │       ├── ConfigReader.java
│   │       └── AllureEnvironmentWriter.java
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   ├── services/
│       │   ├── tests/
│       │   └── utils/
│       └── resources/
│           ├── config-dev.properties
│           ├── config-qa.properties
│           ├── config-prod.properties
├── pom.xml
├── testng.xml
└── README.md
```

---

## How to Run Tests

Run with default (QA) environment:
```bash
mvn clean test
```

Specify environment:
```bash
mvn clean test -Denv=dev
mvn clean test -Denv=prod
```

---

## Generate Allure Report

After running the tests:
```bash
allure serve allure-results
```

This will open the interactive HTML report.

---

## Environment Metadata in Allure

The report includes details such as:
- Environment (dev/qa/prod)
- Base URL
- Tester name
- Build version

These are auto-injected by `AllureEnvironmentWriter.java`.

---

## Author

Bathala Manoj
