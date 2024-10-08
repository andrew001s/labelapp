# Backend para Gestión de Etiquetado de Imágenes

Este proyecto es un backend desarrollado con Spring Boot, diseñado para facilitar la gestión de datos de etiquetado de imágenes y su conversión al formato YOLOv5.

## Requisitos Previos
![Static Badge](https://badgen.net/badge/17/java/orange?icon=java)
![Static Badge](https://img.shields.io/badge/SpringBoot-3.3.0-version)

Para ejecutar y desarrollar este backend, necesitarás:

- Jdk 17 o superior
- Spring Boot 3.3.0 o superior


## Configuración e Instalación

Sigue estos pasos para configurar el proyecto en tu entorno local:

1. Clona el repositorio a tu máquina local usando
   ```bash
   git clone https://github.com/andrew001s/labelapp
3. Abre el proyecto en tu IDE preferido.
4. Asegúrate de que el IDE reconozca el archivo `pom.xml` para gestionar las dependencias.

# Configuración Proyecto
En src/resources/application.propierties
```java
spring.application.name=labelapp
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
spring.data.mongodb.uri=mongodb+srv://YourDatabaseUri/
spring.data.mongodb.database=YourDabaseName
file.upload-dir=YourUploadDirectory
```
Ajusta los valores para
<ul>
   <li><strong>spring.data.mongodb.uri: </strong>El Uri de tu base de datos Mongo</li>
   <li><strong>spring.data.mongodb.database: </strong>El Nombre de tu base de datos</li>
   <li><strong>file.upload-dir: </strong>El url o directorio de tu cdn</li>
</ul>

## Uso del Backend

Una vez que el backend está en funcionamiento, puedes:

1. Interactuar con la API REST para realizar operaciones CRUD sobre las etiquetas de imágenes.
2. Utilizar los endpoints específicos para convertir los datos de etiquetado al formato YOLOv5.


