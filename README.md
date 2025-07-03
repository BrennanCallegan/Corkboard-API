# Corkboard-API

This is a RESTful API for [Corkboard](https://github.com/BrennanCallegan/Corkboard), a Summer of Coding project in development.

## Features

- **RESTful Endpoints**: Exposes endpoints for creating, retrieving, updating, and deleting notes.
- **Data Persistence**: Stores note data in a PostgreSQL database.
- **Database Migrations**: Manual SQL script for schema management.
- **ID Generation**: Uses PostgreSQL sequences for robust primary key generation.
- **Timestamping**: Automatic createdAt and updatedAt management.
- **Validation**: Basic input validation for note fields.

## Tech Stack

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA
- PostgreSQL
- Gradle (for build management)
- Docker (for running PostgreSQL locally)

## Setting Up the API

**1. Prerequisites**
- JDK 17 or newer
- Docker Desktop
- IntelliJ IDEA Community (recommended) with Database Navigator plugin
- Postman (or similar API client)

**2. Database Setup with Docker**
Corkboard-API uses Docker to run a persistent PostgreSQL database.
1. **Stop and Remove Old Containers (Initial Setup Only):**
   
If you've run PostgreSQL containers before without a volume:
- Open your terminal/command prompt
- List all containers (including stopped ones)
```bash
docker ps -a
```
- Find any PostgreSQL containers. Note their CONTAINER ID or NAMES.
- Stop them (if running): *docker stop <container_id_or_name>*
- Remove them: *docker rm <container_id_or_name>*

2. **Start PostgreSQL with a Persistent Docker Volume**
   
This command starts PostgreSQL in the background and saves its data to a Docker volume named postgres-data, which allows you to use *docker start postgres-data* rather than spinning up a new container next time you run the code.
```bash
docker run -d \
  -p 5432:5432 \
  --name my-postgres-db \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=postgres \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:17
```
3. **Verify that the Container is Running**
```bash
docker ps
```
**3. Database Schema Creation**

Now, we'll create the necessary tables and sequences in your PostgreSQL database.

  1. Open IntelliJ IDEA and ensure your project is loaded.

  2. Open Database Navigator: In IntelliJ, open the "Database Navigator" tool window (usually on the left or right sidebar).

  3. Connect to your PostgreSQL Database:

      - Right-click on your existing "Notes" (or similar) connection.

      - Ensure it's configured to connect to jdbc:postgresql://localhost:5432/postgres with username=postgres and password=postgres.

      - Click "Connect" if it's not already connected.

  4. Execute the SQL Script:

      - Ensure the script includes *DROP TABLE IF EXISTS notes CASCADE;* and *DROP SEQUENCE IF EXISTS note_id_seq;* at the top for safe re-execution.

      - Select the entire script text (Ctrl+A / Cmd+A).

      - Click the green play button in the SQL editor toolbar to execute the selected text.

      - Crucially, explicitly commit the changes. In Database Navigator, after execution, look for a "Commit" button or option (often a green checkmark icon, or right-click in the editor and find "Commit"). This ensures your schema changes are saved to the persistent Docker volume.

**4. Build and Run the Spring Boot Application**

  1. Perform a Clean Build (after code changes):

  - In IntelliJ, go to *File > Invalidate Caches / Restart...* and select "Invalidate and Restart." (Recommended if you've faced odd build issues).

  - After IntelliJ restarts, open the Gradle tool window (usually on the right sidebar).

  - Expand your project, then *Tasks*, then *build*.

  - Double-click on *clean*, then double-click on *build*. Confirm both complete successfully.

  - Alternatively, from your project's server directory in the terminal: *./gradlew clean build*

  2. Run the Application:

      - Locate *ServerApplication.java* in your project.

      - Run the *main* method directly from IntelliJ (using the green play arrow next to it).

**5. Testing the API with Postman**

Once the Spring Boot application has started successfully (look for "Started ServerApplication in ..." in the console), you can test your endpoints.

  - GET all notes:

      - Method: GET

      - URL: http://localhost:8080/api/notes

      Expected Response: 200 OK and a JSON array of your notes.

  - POST (Create) a new note:

      - Method: POST

      - URL: http://localhost:8080/api/notes

      Headers: Content-Type: application/json

      Body: Select raw and JSON

      Example JSON Body:

```JSON
{
    "title": "My New Note",
    "body": "This is the content of my new note."
}
```
**Expected Response:** *201 Created* and potentially the created note object.

## Troubleshooting
*ERROR: relation "notes" does not exist or ERROR: relation "note_id_seq" does not exist*
- This means your database schema is not present. Re-do Step 3 (Database Schema Creation) carefully, ensuring you execute the entire script and commit the changes. Also, verify your docker ps shows the my-postgres-db container is Up.

*Could not instantiate id generator [...]*
- This is a Java entity mapping issue. Double-check your Notes.java annotations (@Table(name = "notes"), @SequenceGenerator(name = "notes_id_gen", sequenceName = "note_id_seq", allocationSize = 50)) and perform a Step 4.1 (Clean Build).

*Required request body is missing* (400 error on POST):
You forgot to set the request body in Postman. Ensure you select "raw" and "JSON" under the "Body" tab and provide a valid JSON payload.

*column n1_0.body does not exist*:
- Your Java entity is looking for body, but the database still has url. Re-do Step 3 (Database Schema Creation) with the correct SQL script and make sure you committed. Also, double-check your Notes.java has @Column(name = "body").





















