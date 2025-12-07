-- V2__init_clients.sql
-- тестовые клиенты

INSERT INTO dbo.clients (full_name, birth_date, address, phone, email)
VALUES
    (N'Ivan Ivanov', '1990-05-10', N'Frunze 20', N'89643321111', N'ivanov@test.com'),
    (N'Maxim Chakanov', '1980-02-11', N'Frunze 50', N'89614372481', N'maxchakanov@gmail.com');
