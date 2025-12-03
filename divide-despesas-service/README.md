# Divide Despesas Service - Docker

## Configuração

Certifique-se de que o arquivo `.env` contém suas credenciais AWS:

```env
AWS_ACCESS_KEY_ID=sua_key
AWS_SECRET_ACCESS_KEY=sua_secret
```

## Iniciar o serviço

```bash
cd divide-despesas-service
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

- API: http://localhost:8083
- Swagger: http://localhost:8083/swagger-ui.html
