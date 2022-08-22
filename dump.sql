CREATE DATABASE account_manager;

CREATE USER account_manager_user WITH PASSWORD 'account_manager_passwd';

GRANT ALL ON DATABASE account_manager TO account_manager_user;

GRANT pg_read_server_files TO account_manager_user;
