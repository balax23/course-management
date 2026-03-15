# Course Management

## Projekt leírása
Ez a projekt egy kurzuskezelő rendszer, amely a záróvizsga gyakorlati feladatához készült.  
A rendszer Spring Boot, Java 17, MySQL, H2, HTML, CSS és JavaScript használatával lett megvalósítva.

Az alkalmazás lehetővé teszi:
- kurzusok létrehozását, lekérdezését, módosítását és törlését
- felhasználók létrehozását és listázását
- kategóriák létrehozását és listázását
- oktatók hozzárendelését kurzusokhoz
- hallgatók feliratkoztatását kurzusokra

A projekt bemutatja:
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
A projekt főbb csomagjai és mappái:

- `controller` – REST vezérlők
- `service` – üzleti logika
- `repository` – adatbázis elérés
- `entity` – JPA entitások
- `dto` – adatátviteli objektumok
- `mapper` – entitás–DTO leképezés
- `exception` – egyedi kivételkezelés
- `src/main/resources/static` – frontend fájlok
- `src/test` – unit és integration tesztek
- `src/docs` – UML és adatbázis diagramok
- `src/postman` – Postman kollekció

---

## Adatbázisterv
A rendszer fő entitásai:
- `User`
- `Course`
- `Category`

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
- felhasználó létrehozása
- kategória létrehozása
- kurzus létrehozása
- kurzusok listázása
- kurzus törlése
- hallgató feliratkoztatása kurzusra
- dinamikus kiválasztás legördülőkkel és checkboxokkal
- egyszerű statisztikai dashboard

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

## DTO-k
A projekt külön DTO-kat használ az adatátvitelhez.

Példák:
- `CourseCreateRequest`
- `CourseResponse`
- `UserCreateRequest`
- `UserResponse`
- `CategoryCreateRequest`
- `CategoryResponse`

Ez segíti:
- a tisztább rétegszétválasztást
- a validációt
- az entitások közvetlen kiexponálásának elkerülését

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