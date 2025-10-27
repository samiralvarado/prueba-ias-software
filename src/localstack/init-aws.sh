#!/bin/bash

echo "Inicializando LocalStack..."

# Esperar a que LocalStack esté listo
until awslocal dynamodb list-tables; do
  echo "Esperando a que LocalStack esté listo..."
  sleep 2
done

echo "Creando tabla de Machines en DynamoDB..."
awslocal dynamodb create-table \
    --table-name Machines \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST

echo "Creando tabla de Maintenances en DynamoDB..."
awslocal dynamodb create-table \
    --table-name Maintenances \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST

echo "Creando secreto de base de datos..."
awslocal secretsmanager create-secret \
    --name maintenance-db-credentials \
    --secret-string '{"username":"test-user","password":"test-pass","host":"localhost","port":"8000"}'

# Insertar datos de ejemplo simplificados
echo "Insertando datos de ejemplo en Machines..."
awslocal dynamodb put-item \
    --table-name Machines \
    --item '{
        "id": {"S": "machine-001"},
        "name": {"S": "Torno CNC"},
        "location": {"S": "Area A"},
        "createdAt": {"S": "2024-01-01T10:00:00"},
        "updatedAt": {"S": "2024-01-01T10:00:00"}
    }'

awslocal dynamodb put-item \
    --table-name Machines \
    --item '{
        "id": {"S": "machine-002"},
        "name": {"S": "Fresadora"},
        "location": {"S": "Area B"},
        "createdAt": {"S": "2024-01-01T11:00:00"},
        "updatedAt": {"S": "2024-01-01T11:00:00"}
    }'

echo "Insertando datos de ejemplo en Maintenances..."
awslocal dynamodb put-item \
    --table-name Maintenances \
    --item '{
        "id": {"S": "maintenance-001"},
        "machineId": {"S": "machine-001"},
        "type": {"S": "PREVENTIVE"},
        "description": {"S": "Mantenimiento preventivo mensual"},
        "status": {"S": "SCHEDULED"},
        "technicianId": {"S": "tech-001"},
        "createdAt": {"S": "2024-01-01T10:00:00"}
    }'

echo "✅ LocalStack inicializado correctamente"
echo "📊 Tablas creadas:"
awslocal dynamodb list-tables