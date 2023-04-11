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
2. Create a `.env` file in the `backend/` **sub**folder of the current directory, it should look like this:
```ini
SECRET_KEY=<see note below>
DB_NAME=fibo
DB_USER=fibo
DB_PASSWORD=*snip*
DB_HOST=127.0.0.1
DB_PORT=5432
```
The `SECRET_KEY` should be sufficiently random. For example, you can use the following node.js script and set the `SECRET_KEY` to the result of it: `require('crypto').randomBytes(64).toString('hex')`.

Update the `DB_*` variables as needed, so they use the prepared PostgreSQL installation.

3. Run `docker compose run django python manage.py makemigrations`
4. Run `docker compose run django python manage.py migrate`
4. Run `docker compose run django python manage.py collectstatic`

From now on, you can use the command `docker compose up -d --build` to have the Django backend run.