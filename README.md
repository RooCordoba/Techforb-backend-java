# Techforb-backend-java
Repositorio para el Challenge de la posicion Dev. Backend de TECHFORB con Java Spring Boot

Simula el manejo de un sistema Bancario, con creacion de Usuarios en Base de datos, las tarjetas que generen y las transacciones que se realicen.

### Usuarios

* Creacion de Usuarios
* Inicio de Sesion
* Cerrar Sesion
* Eliminar Usuario
* Obtener Todos los Usuarios

### Tarjetas

* Pedir Tarjeta
* Ver Datos de una tarjeta
* Eliminar Tarjeta

### Transacciones

* Depositar Dinero
* Extraer Dinero
* Transferir Dinero
* Ver historial de Transacciones

## Prerequisitos

* Tener instalado [java jdk](https://jdk.java.net/) (el proyecto usa la version 20)
* Tener instalado [Apache Maven](https://maven.apache.org/download.cgi)

* Instalar [postgreSQL](https://www.postgresql.org/download/):
   * En la instalacion, dejar el puerto 5432, usuario: postgres y contraseña 1234.
   * En caso de ya tener otro puerto, usuario o contraseña configurados, modificar **application.properties** (dentro de src/main/resources) en las lineas:

    - spring.datasource.url=jdbc:postgresql://localhost:5432/techforbDB
    - spring.datasource.username=postgres
    - spring.datasource.password=1234

* Iniciar pgAdmin (que se instala junto con el postgresql) y colocar la contraseña 1234 o la configurada por el usuario.
* Dentro del pgAdmin, crear una base de datos llamada: techforbDB

## Clonar Repositorio

```
git clone https://github.com/RooCordoba/Techforb-backend-java.git
```
```
cd Techforb-backend-java
```

## Para correr el programa

* Tener el pdAdmin corriendo.
* Dentro de la carpeta raiz del proyecto, abrir una terminal y escribir el siguiente comando:

```
./mvnw spring-boot:run
```

Se iniciara el programa en [localhost:8080/swagger-ui/index](http://localhost:8080/swagger-ui/index.html). Para interactuar con los endpoints, ir a esa direccion.

**Para detener el programa, apretar 'Ctrl + C'**  

## Tecnologías utilizadas

* Java
* Spring Boot
* Maven
* Springdoc-openapi (para una interfaz mas amigable al usuario para interactuar con los endpoints)
* PostgreSQL (manejo de la base de datos)

### Imagenes de muestra del proyecto

![Image Text](https://github.com/RooCordoba/Techforb-backend-java/blob/develop/src/main/java/com/ar/techforb/imagenes-github/Endpoints-imagen.png)