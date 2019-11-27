# rps-to-the-max

## Note
- You must have a running instance of PostgreSQL. `rps` database and user with
  superpower must be created too.
```
$ pg_ctl -D /usr/local/var/postgres start
$ psql postgres
postgres=# create user rps with superuser;
postgres=# create database rps;
```
