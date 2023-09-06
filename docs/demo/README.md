DEMO Registry service
=====================

## Dependencies

1. `docker`, `docker-compose`

Run `docker-compose up -d` to set up test SpiceDB, Keycloak servers and their Databases.

## Running

```
mvn compile exec:java -Dexec.mainClass="com.example.App"
```

### Formatting Sources
```
mvn prettier:write
```

### Running Tests
```
mvn test
```
