# eCommerce App 🛒

**Descripción:**

Esta tienda en línea fue desarrollada utilizando las siguientes tecnologías:

- **Angular v18:** Para construir un frontend dinámico como Single Page Application (SPA).
- **Angular Material:** Para componentes clave como el carrito de compras y los botones.
- **Java v23 con Spring Boot 3.4:** Para el backend que facilita la comunicación y gestión de datos.
- **MySQL (en la nube):** Base de datos relacional para almacenar datos del eCommerce.
- **JPA con Hibernate:** ORM para interactuar con la base de datos MySQL de manera eficiente.
  

Esta combinación tecnológica permitió crear una plataforma eficiente y escalable, asegurando una experiencia de compra fluida y moderna.

## Características

- Catálogo de productos interactivo: Muestra de productos con detalles y funcionalidades de búsqueda.
- Funcionalidad de carrito de compras: Permite agregar productos y realizar compras de manera intuitiva.
- CRUD (Create, Read, Update, Delete): Implementado para gestionar productos, usuarios y pedidos en la base de datos.
- Diseño moderno y atractivo: Utilizando Angular Material para una experiencia de usuario mejorada.
- Backend robusto con Java y Spring Boot: Conectado a una base de datos MySQL en la nube a través de JPA con Hibernate.
- Inicio de sesión y manejo de sesiones: Utilizando cookies para mantener las sesiones de usuario seguras y persistentes.
- Seguridad en contraseñas: Las contraseñas de los usuarios se encriptan utilizando bcrypt para garantizar su seguridad.


## Requisitos

- Jdk 23
- Maven
- Angular CLI

## Instalación

1. **Instala las dependencias del frontend y del backend:**

    ```bash
    cd frontend
    npm install
    ng serve -o

    cd Backend-javaSpringBoot
    mvn clean install -DskipTests
    mvn spring-boot:run
    ```

## Contacto

Para más información o dudas, puedes contactarme en [peperj7@gmail.com](mailto:peperj7@gmail.com).
