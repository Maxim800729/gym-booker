-- V3__coaches_and_session_coach.sql

-- rooms
INSERT INTO dbo.rooms (name, capacity)
VALUES
    (N'Большой зал', 20),
    (N'Малый зал', 16);

-- coaches
INSERT INTO dbo.coaches (full_name, phone)
VALUES
    (N'Тренер 1', N'+70000000001'),
    (N'Тренер 2', N'+70000000002');

-- class types
INSERT INTO dbo.class_types (title, duration_min)
VALUES
    (N'Тяжелая атлетика', 60),
    (N'Кроссфит', 60),
    (N'ОФП', 45);

-- Получим id по названиям
DECLARE @room1 BIGINT = (SELECT id FROM dbo.rooms WHERE name = N'Большой зал');
DECLARE @room2 BIGINT = (SELECT id FROM dbo.rooms WHERE name = N'Малый зал');

DECLARE @coach1 BIGINT = (SELECT id FROM dbo.coaches WHERE full_name = N'Тренер 1');
DECLARE @coach2 BIGINT = (SELECT id FROM dbo.coaches WHERE full_name = N'Тренер 2');

DECLARE @wt  BIGINT = (SELECT id FROM dbo.class_types WHERE title = N'Тяжелая атлетика');
DECLARE @cf  BIGINT = (SELECT id FROM dbo.class_types WHERE title = N'Кроссфит');
DECLARE @ofp BIGINT = (SELECT id FROM dbo.class_types WHERE title = N'ОФП');

-- расписание
INSERT INTO dbo.class_sessions (class_type_id, room_id, coach_id, start_at, capacity)
VALUES
    -- Coach 1
    (@wt,  @room1, @coach1, '2025-12-08T10:00:00+00:00', 20),
    (@cf,  @room1, @coach1, '2025-12-08T12:00:00+00:00', 20),
    (@ofp, @room1, @coach1, '2025-12-08T18:00:00+00:00', 20),

    -- Coach 2
    (@wt,  @room2, @coach2, '2025-12-08T11:00:00+00:00', 16),
    (@cf,  @room2, @coach2, '2025-12-08T15:00:00+00:00', 16),
    (@ofp, @room2, @coach2, '2025-12-08T19:00:00+00:00', 16);
