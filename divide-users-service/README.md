# Divide Users Service - Docker

## Iniciar o serviço

```bash
cd divide-users-service
docker-compose up -d --build
```

## Ver logs

```bash
docker-compose logs -f
```

## Parar o serviço

```bash
docker-compose down
```

## Endpoints

- API: http://localhost:8082
- Swagger: http://localhost:8082/swagger-ui.html
- PostgreSQL: localhost:5432
