#!/bin/bash

# Set the repository URL and branch name
repo_url="https://github.com/Cebox82/FiBo.git"
branch="master"

# Set the local directory where you want to clone the repository
local_dir="$HOME/fibo/"

# Check if the local directory exists, if not, create it
if [ ! -d "$local_dir" ]; then
    mkdir -p "$local_dir"
fi

# Navigate to the local directory
cd "$local_dir" || exit

# Check if the repository exists, if not, clone it
if [ ! -d ".git" ]; then
    git clone "$repo_url" .
fi

# Fetch the latest changes from the remote repository
git fetch

# Check if there are any changes on the specified branch
if git diff --quiet origin/"$branch"; then
    echo "No changes found."
else
    # Pull the latest changes if there are any
    git pull origin "$branch"

    # Update Docker after Pulling the changes
    cd "$local_dir/backend" || exit
    sudo docker compose up -d --build

    # Make migrations in case the database has changed
    rm -v "$local_dir/backend/api/migrations/0*"
    docker compose run --rm django python manage.py makemigrations
    docker compose run --rm django python manage.py migrate
    docker compose run --rm django python manage.py collectstatic
fi
