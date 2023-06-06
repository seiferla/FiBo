# FiBo Backend

## Setting up a development environment
Prerequisites:
- Repository is cloned
- Python (3.11) and pip are installed
- Have a PostgreSQL database ready

If you are using MacOS run these Steps first:
1. Run `brew install openssl libpq`
2. Add the following lines to the `~/.zshrc` file
```ini
export PATH="/opt/homebrew/opt/openssl@3/bin:$PATH"
export LDFLAGS="-L/opt/homebrew/opt/openssl@3/lib $LDFLAGS"
export CPPFLAGS="-I/opt/homebrew/opt/openssl@3/include $CPPFLAGS"
export PATH="/opt/homebrew/opt/libpq/bin:$PATH"
export LDFLAGS="-L/opt/homebrew/opt/libpq/lib $LDFLAGS"
export CPPFLAGS="-I/opt/homebrew/opt/libpq/include $CPPFLAGS"
```
3. Open a new Terminal so the changes to `~/.zshrc` get sourced

If you followed the instructions above or are using a different OS run the following steps:
1. Set your current working directory to the directory where this `README.md` is located: `cd backend/`
2. Create a `.env` file in the `backend/` **sub**folder of the current directory, it should look something like this:
```ini
SECRET_KEY=<see note below>
SIGNING_KEY=<see note below>
POSTGRES_DB=fibo
POSTGRES_USER=*snip*
POSTGRES_PASSWORD=*snip*
DB_HOST=postgres
DB_PORT=5432

PGADMIN_DEFAULT_EMAIL=*snip*
PGADMIN_DEFAULT_PASSWORD=*snip*
```
The `SECRET_KEY` and the `SIGNING_KEY` should be sufficiently random. For example, you can use the following node.js script and set the `SECRET_KEY` and `SIGNING_KEY` to two seperate result of it: `require('crypto').randomBytes(64).toString('hex')`.

Update the `POSTGRES_*`,`PGADMIN_DEFAULT_*` variables as needed, so they use the prepared PostgreSQL installation.

3. Run `docker compose run --rm django python manage.py makemigrations`
4. Run `docker compose run --rm django python manage.py migrate`
5. Run `docker compose run --rm django python manage.py collectstatic`

From now on, you can use the command `sudo docker compose up -d --build` to have the Django backend run on port 8000.

To stop the backend, run the command `sudo docker compose stop`.

Troubleshooting:

You may need to run `rm ~/.docker/config.json` in case you are seeing `ERROR [internal] load metadata for docker.io/library/python:3` when doing the steps

If the "docker compose run django python manage.py ..." commands fail due to password authentication you might need to create a new user:

1. Run `docker exec -it <container-name> psql -U postgres -d <env-database-name> -c "CREATE USER <env-username> WITH PASSWORD '<env-password>';"`
2. Run `docker exec -it <container-name> psql -U postgres -d <env-database-name> -c "GRANT ALL PRIVILEGES ON SCHEMA public TO <env-username>;"`


## Create Django UML Diagram
In order to create a UML-Diagram with Django som additional steps are needed.
The following Steps are for MacOS only and wont work on Windows!
Open a Terminal and run the following commands:
1. Run `brew install graphviz`
2. Run `pip install graphviz`
3. Run `python -m pip install --global-option=build_ext --global-option="-I$(brew --prefix graphviz)/include/" --global-option="-L$(brew --prefix graphviz)/lib/" pygraphviz`

After that you should be able to create UML Diagramms with the following command:
`python manage.py graph_models -a -g -o ../docs/ER_Model_Uml.png`

## Running Tests

In order to run the Tests make sure that your Docker Containers are running. You can start the containers with the command `sudo docker compose up -d --build`

To run the Tests use the command:
`docker compose run --rm django coverage run manage.py test`

For a more detailed output about what tests where run you can add an `-v 2` to the previous command. It will now look like this:
`docker compose run --rm django coverage run manage.py test -v 2`

You can output the test coverage in two different way:
1. With `docker compose run --rm django coverage report` the test coverage is printed to the console
2. With `docker compose run --rm django coverage html -d docs/test-coverage-report` an HTML Coverage report is created in the directory `docs/test-coverage-report`
To look at it simply open the index.html in your preferred browser. In there you can go through the different files and see which code blocks got already tested and which can use some additional testing.

## Install backend on server with continuous deployment
To install the latest version of the backend continuously you have to create a cron job on your server. To do this run the command
`crontab -e`. Now you have to choose which text editor you want to use. Select your preferred one and add the following line at the bottom of the file:
```ini
0 0 * * * chmod +x $HOME/fibo/backend/server_deployment.sh && $HOME/fibo/backend/server_deployment.sh
```
After this save the file. Now the cron job will check for a new version every night and will deploy it if there is one available.
