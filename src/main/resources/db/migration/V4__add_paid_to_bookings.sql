-- V4__add_paid_to_bookings.sql

IF COL_LENGTH('dbo.bookings', 'paid') IS NULL
BEGIN
ALTER TABLE dbo.bookings
    ADD paid bit NOT NULL
    CONSTRAINT DF_bookings_paid DEFAULT (0);
END;

IF COL_LENGTH('dbo.bookings', 'paid_until') IS NULL
BEGIN
    IF COL_LENGTH('dbo.bookings', 'paid_to') IS NOT NULL
BEGIN
EXEC sp_rename 'dbo.bookings.paid_to', 'paid_until', 'COLUMN';
END
ELSE
BEGIN
ALTER TABLE dbo.bookings
    ADD paid_until datetimeoffset(6) NULL;
END
END;

ALTER TABLE dbo.bookings
ALTER COLUMN paid_until datetimeoffset(6) NULL;
