# Course Management System

## Projekt leírása
Ez a projekt egy kurzuskezelő rendszer, amely Spring Boot, Java 17, MySQL, H2, HTML, CSS és JavaScript használatával készült.

Az alkalmazás lehetővé teszi:
- kurzusok létrehozását és kezelését
- felhasználók létrehozását és listázását
- kategóriák létrehozását és listázását
- oktatók hozzárendelését kurzusokhoz
- hallgatók feliratkoztatását kurzusokra

A rendszer bemutatja:
- a réteges architektúrát
- DTO-k használatát
- REST API fejlesztést
- kivételkezelést
- validációt
- naplózást
- unit teszteket
- integration teszteket
- frontend integrációt

---

## Felhasznált technológiák
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- H2 adatbázis
- Lombok
- Maven
- HTML
- CSS
- JavaScript
- Postman
- JUnit 5
- Mockito
- MockMvc

---

## Projekt felépítése
- `controller` – REST vezérlők
- `service` – üzleti logika
- `repository` – adatbázis elérés
- `entity` – JPA entitások
- `dto` – adatátviteli objektumok
- `mapper` – entitás–DTO leképezés
- `exception` – egyedi kivételkezelés
- `static` – frontend fájlok
- `test` – unit és integration tesztek

---

## Adatbázisterv
Fő entitások:
- User
- Course
- Category

Kapcsolatok:
- Egy oktató több kurzust is tarthat (`ManyToOne`)
- Egy kurzus több kategóriához is tartozhat (`ManyToMany`)
- Egy kurzusra több hallgató is feliratkozhat (`ManyToMany`)

---

## Funkciók

### Backend
- kurzusok létrehozása, lekérdezése, módosítása, törlése
- felhasználók létrehozása és listázása
- kategóriák létrehozása és listázása
- hallgatók feliratkoztatása kurzusokra
- validáció és kivételkezelés
- naplózás SLF4J használatával

### Frontend
- kurzus létrehozása
- kurzusok listázása
- kurzus törlése
- hallgató feliratkoztatása kurzusra

### Tesztelés
- unit tesztek a service réteghez
- integration tesztek a controller réteghez H2 adatbázissal

---

## API végpontok

### User végpontok
- `POST /api/users`
- `GET /api/users`

### Category végpontok
- `POST /api/categories`
- `GET /api/categories`

### Course végpontok
- `POST /api/courses`
- `GET /api/courses`
- `GET /api/courses/{id}`
- `PUT /api/courses/{id}`
- `DELETE /api/courses/{id}`
- `POST /api/courses/{courseId}/enroll/{studentId}`

---

## Az alkalmazás futtatása

### Előfeltételek
- Java 17
- Maven
- MySQL szerver

### MySQL beállítás
Hozz létre egy `course_management` nevű adatbázist:

```sql
CREATE DATABASE course_management;
Az alkalmazás az adatbázis felhasználónevét és jelszavát környezeti változókból olvassa be.

Az `application.properties` fájlban ez szerepel:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/course_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}