
<img src="https://github.com/user-attachments/assets/8c96711e-f640-4c2b-8284-a052219f719b" alt="drawing" width="200"/>

# Blood Manager Backend
Blood Manager is an open-source, inofficial web tool to track [Blood on the Clocktower](https://bloodontheclocktower.com/) games.
This repository contains the Spring Boot backend. You can find the frontend [here](https://github.com/tikelespike/gamestats-frontend).

Please note that this project is a very early work in progress.
The vision is that this tool will allow groups to track their games, player statistics like win rate or rate of being in good/evil team, characters already played, achievements, a level/xp system...

## Running the Backend
Build the project using gradle:

```bash
./gradlew clean build
```

Blood Manager stores its data in a PostgreSQL database. If you want, you can use the provided `docker-compose.yml` to create one automatically.

Please note that you need to supply a number of environment variables (e.g. via an `.env` file, which is referenced from the `docker-compose.yml`) to configure the service (mostly the database connection). See the example configuration for local testing.

Once you have configured the environment variables (or used the example file), you can for example run (from the project directory):

```bash
docker compose up --build # build the application image and run both the database and the application
```

or, if you are using another database, just build the application image and run it with docker:

```bash
docker build -t bloodmanager .
docker run -it bloodmanager
```

Once running, the API is available under the port specified in the `PORT` environment variable (for example `http://localhost:8080/api/v1` if you are using the default local configuration)

## Documentation
The Blood Manager backend is a gradle project using Spring Boot and PostgreSQL.
The project is structured into three layers (API, business logic, and data source).
In addition to the JavaDoc code documentation, there is a swagger API documentation available. Visit `http://<application url>/swagger-ui/index.html` for the web interface (e.g. `http://localhost:8080/swagger-ui/index.html` if running locally)
