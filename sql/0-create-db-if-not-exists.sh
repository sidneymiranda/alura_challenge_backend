#!/bin/bash

psql -U account_manager_user -tc "SELECT 1 FROM pg_database WHERE datname = 'account_manager'" \
| grep -q 1 || psql -U account_manager_user -c "CREATE DATABASE account_manager"