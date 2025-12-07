-- V1__init.sql
-- Базовая схема проекта (SQL Server)

-- rooms
CREATE TABLE dbo.rooms (
                           id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           name NVARCHAR(100) NOT NULL,
                           capacity INT NOT NULL
);

-- coaches
CREATE TABLE dbo.coaches (
                             id BIGINT IDENTITY(1,1) PRIMARY KEY,
                             full_name NVARCHAR(200) NOT NULL,
                             phone NVARCHAR(50) NULL,
                             avatar_url NVARCHAR(500) NULL,
                             bio NVARCHAR(1000) NULL,
                             created_at DATETIME2(6) NOT NULL DEFAULT SYSDATETIME()
);

-- class types
CREATE TABLE dbo.class_types (
                                 id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                 title NVARCHAR(150) NOT NULL,
                                 description NVARCHAR(500) NULL,
                                 duration_min INT NOT NULL,
                                 created_at DATETIME2(6) NOT NULL DEFAULT SYSDATETIME()
);

-- clients
CREATE TABLE dbo.clients (
                             id BIGINT IDENTITY(1,1) PRIMARY KEY,
                             full_name NVARCHAR(200) NULL,
                             birth_date DATE NULL,
                             address NVARCHAR(255) NULL,
                             phone NVARCHAR(50) NULL,
                             email NVARCHAR(255) NOT NULL,
                             avatar_url NVARCHAR(500) NULL,
                             created_at DATETIME2(6) NOT NULL DEFAULT SYSDATETIME(),
                             CONSTRAINT UQ_clients_email UNIQUE (email)
);

-- class sessions
CREATE TABLE dbo.class_sessions (
                                    id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                    class_type_id BIGINT NOT NULL,
                                    room_id BIGINT NOT NULL,
                                    coach_id BIGINT NOT NULL,
                                    start_at DATETIMEOFFSET(6) NOT NULL,
                                    capacity INT NOT NULL,
                                    created_at DATETIMEOFFSET(6) NOT NULL DEFAULT SYSDATETIMEOFFSET(),

                                    CONSTRAINT FK_sessions_type  FOREIGN KEY (class_type_id) REFERENCES dbo.class_types(id),
                                    CONSTRAINT FK_sessions_room  FOREIGN KEY (room_id) REFERENCES dbo.rooms(id),
                                    CONSTRAINT FK_sessions_coach FOREIGN KEY (coach_id) REFERENCES dbo.coaches(id)
);

-- bookings
CREATE TABLE dbo.bookings (
                              id BIGINT IDENTITY(1,1) PRIMARY KEY,
                              session_id BIGINT NOT NULL,
                              user_email NVARCHAR(255) NOT NULL,
                              status NVARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                              created_at DATETIMEOFFSET(6) NOT NULL DEFAULT SYSDATETIMEOFFSET(),

    -- paid / paid_until добавятся в V4

                              CONSTRAINT FK_bookings_session FOREIGN KEY (session_id) REFERENCES dbo.class_sessions(id),
                              CONSTRAINT UQ_booking_unique UNIQUE (session_id, user_email)
);
