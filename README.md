Maintenance Reactive API
Una API reactiva moderna para sistemas de mantenimiento construida con Spring Boot, WebFlux, DynamoDB y seguridad JWT.

📋 Tabla de Contenidos
Características

Tecnologías

Prerrequisitos

Instalación Rápida

Configuración

Estructura del Proyecto

Uso

API Endpoints

Desarrollo

Testing

Despliegue

🚀 Características
Arquitectura Reactiva: Totalmente no bloqueante usando Spring WebFlux

Base de datos NoSQL: DynamoDB con Spring Data DynamoDB

Desarrollo Local: AWS LocalStack para desarrollo

Autenticación JWT: Seguridad basada en tokens

Múltiples Roles: Technician, Supervisor y SuperAdmin

API RESTful: Diseño limpio y escalable

🛠 Tecnologías

Java 21

Spring Boot 3.x

Spring WebFlux (Reactivo)

Spring Security con JWT

Spring Cloud AWS + DynamoDB

Project Reactor

LocalStack (AWS local)

JUnit 5 + Mockito

Docker + Docker Compose

📋 Prerrequisitos
Java 21

Maven 3.6+

Docker y Docker Compose

AWS CLI (opcional, para debugging)

🚀 Instalación Rápida
1. Clonar el repositorio
   bash
   git clone https://github.com/samiralvarado/prueba-ias-software.git
   cd maintenance-reactive-api
2. Iniciar Infraestructura con Docker Compose
   bash
   docker-compose up -d
3. Verificar que LocalStack esté funcionando
   bash
   curl http://localhost:4566/health
4. Compilar y ejecutar la aplicación
   bash
   mvn clean compile
   mvn spring-boot:run
5. Verificar que la aplicación esté corriendo
   bash
   curl http://localhost:8080/actuator/health
   🔧 Configuración
   Archivo de Configuración Principal
   El archivo application.yml está configurado para desarrollo local:

yaml
spring:
main:
allow-bean-definition-overriding: true
application:
name: maintenance-reactive-api
cloud:
aws:
region:
static: us-east-1
credentials:
access-key: test
secret-key: test
dynamodb:
endpoint: http://localhost:4566
enabled: true
data:
dynamodb:
entity2ddl:
auto: create
endpoint: http://localhost:4566
region: us-east-1

jwt:
secret: mySuperSecretKeyForJWTTokenGenerationInMaintenanceApp2024

aws:
secrets:
database: '{"username":"test-user","password":"test-pass","host":"localhost","port":"5432","database":"maintenance_db"}'

logging:
level:
com.company.maintenance: DEBUG
io.awspring.cloud: DEBUG
org.springframework.data.dynamodb: DEBUG
software.amazon.awssdk: DEBUG
com.amazonaws: DEBUG

server:
port: 8080
Docker Compose para LocalStack
Crea un archivo docker-compose.yml en la raíz:

yaml
version: '3.8'
services:
localstack:
image: localstack/localstack:latest
ports:
- "4566:4566"
environment:
- SERVICES=dynamodb,secretsmanager
- DEBUG=1
- DOCKER_HOST=unix:///var/run/docker.sock
volumes:
- "/var/run/docker.sock:/var/run/docker.sock"
- "./localstack_data:/tmp/localstack"
Script de Inicialización (opcional)
Crea un script scripts/init-localstack.sh:

bash
#!/bin/bash

echo "Initializing LocalStack..."

# Crear tabla en DynamoDB
aws dynamodb create-table \
--endpoint-url http://localhost:4566 \
--table-name MaintenanceOrders \
--attribute-definitions \
AttributeName=id,AttributeType=S \
AttributeName=createdAt,AttributeType=S \
--key-schema \
AttributeName=id,KeyType=HASH \
AttributeName=createdAt,KeyType=RANGE \
--billing-mode PAY_PER_REQUEST \
--region us-east-1

echo "LocalStack initialization completed!"
📁 Estructura del Proyecto
text
src/
├── main/
│   ├── java/
│   │   └── com/company/maintenance_reactive_api/
│   │       ├── domain/
│   │       │   ├── constants/
│   │       │   │   └── RoleConstants.java
│   │       │   ├── model/
│   │       │   └── service/
│   │       ├── infrastructure/
│   │       │   ├── adapter/
│   │       │   │   ├── in/
│   │       │   │   │   └── controller/
│   │       │   │   │       ├── dto/
│   │       │   │   │       └── AuthController.java
│   │       │   │   └── out/
│   │       │   │       ├── persistence/
│   │       │   │       │   └── dynamodb/
│   │       │   │       │       ├── entity/
│   │       │   │       │       ├── repository/
│   │       │   │       │       └── config/
│   │       │   │       └── security/
│   │       │   │           └── JWTService.java
│   │       │   └── config/
│   │       └── MaintenanceReactiveApiApplication.java
│   └── resources/
│       ├── application.yml
│       └── application-{profile}.yml
└── test/
└── com/company/maintenance_reactive_api/
└── infrastructure/adapter/in/controller/
└── AuthControllerTest.java
🔐 Uso
Autenticación
Obtener Token JWT

bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
"username": "admin",
"password": "password"
}'
Usar Token en Requests

bash
curl -X GET http://localhost:8080/api/maintenance/orders \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
Usuarios Predefinidos
Usuario	Contraseña	Rol	Permisos
admin	password	TECHNICIAN	Operaciones básicas
supervisor	password	SUPERVISOR	Supervisión + operaciones
root	password	SUPERADMIN	Acceso completo
📡 API Endpoints
Autenticación
POST /api/auth/login - Iniciar sesión

GET /api/auth/validate - Validar token

Órdenes de Mantenimiento

POST /api/maintenance/orders - Crear orden

GET /api/maintenance/orders/{type} - Obtener orden

PUT /api/maintenance/orders/{id} - Actualizar orden


🛠 Desarrollo
Comandos Útiles
bash
# Ejecutar tests
mvn test

# Ejecutar con perfil de desarrollo
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Construir JAR
mvn clean package

# Ver tablas en DynamoDB local
aws dynamodb list-tables --endpoint-url http://localhost:4566
Debugging LocalStack
bash
# Ver logs de LocalStack
docker-compose logs localstack

# Listar tablas DynamoDB
aws dynamodb list-tables --endpoint-url http://localhost:4566

# Describir tabla
aws dynamodb describe-table \
--table-name MaintenanceOrders \
--endpoint-url http://localhost:4566
🧪 Testing
bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests específicos
mvn test -Dtest=AuthControllerTest

# Ejecutar con cobertura
mvn jacoco:report
🐳 Despliegue en Producción
Para producción, actualiza la configuración:

yaml
spring:
cloud:
aws:
credentials:
access-key: ${AWS_ACCESS_KEY_ID}
secret-key: ${AWS_SECRET_ACCESS_KEY}
dynamodb:
endpoint: # Remover para usar AWS real
data:
dynamodb:
endpoint: # Remover para usar AWS real
🔍 Monitoreo y Logs
La aplicación incluye logging detallado:

DEBUG: com.company.maintenance

DEBUG: Operaciones de AWS y DynamoDB

Health Check: http://localhost:8080/actuator/health

🆘 Solución de Problemas
Problemas Comunes
LocalStack no inicia

bash
docker-compose down
docker-compose up -d
Error de conexión a DynamoDB

Verifica que LocalStack esté corriendo en puerto 4566

Revisa los logs: docker-compose logs localstack

Problemas de autenticación

Verifica el secret JWT en la configuración

Revisa que los usuarios tengan las credenciales correctas

Logs de Debug
Para más información, habilita logs detallados modificando el nivel a DEBUG en la configuración.

