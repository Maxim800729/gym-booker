-- V5__add_password_to_clients.sql

IF COL_LENGTH('dbo.clients', 'password_hash') IS NULL
BEGIN
ALTER TABLE dbo.clients
    ADD password_hash NVARCHAR(255) NULL;
END;
