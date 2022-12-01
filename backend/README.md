# FiBo Backend

## Setting up a development environment
Prerequisites:
- Repository is cloned
- Python (3.11) and pip are installed
- Have a PostgreSQL database ready

Run the following steps:
1. Set your current working directory to the directory where this `README.md` is located: `cd backend/`
2. Run `pip install -r requirements.txt`.

On macOS, a successful installation of the dependencies (`pip install`) requires the following libraries that can be installed via brew: `brew install openssl libpq` (please make sure you update the `PATH`, `CPPFLAGS` and `LDFLAGS` environment variables as indicated after each install)

3. Create a `.env` file in the `backend/` **sub**folder of the current directory, it should look like this:
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

4. Run `python manage.py makemigrations`
5. Run `python manage.py migrate`

From now on, you can use the command `python manage.py runserver` to have the Django backend run.