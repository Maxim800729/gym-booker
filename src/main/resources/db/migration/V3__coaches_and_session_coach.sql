-- V3__coaches_and_session_coach.sql
-- Сид: 1 зал, 2 тренера, 3 типа занятий, расписание 2×3

-- =========================
-- ROOM (ТОЛЬКО ОДИН)
-- =========================
IF NOT EXISTS (SELECT 1 FROM dbo.rooms WHERE name = N'Большой зал')
BEGIN
INSERT INTO dbo.rooms (name, capacity)
VALUES (N'Большой зал', 20);
END;

-- =========================
-- COACHES
-- =========================
IF NOT EXISTS (SELECT 1 FROM dbo.coaches WHERE full_name = N'Ригерт Владислав')
BEGIN
INSERT INTO dbo.coaches (full_name, phone)
VALUES (N'Ригерт Владислав', N'+70000000001');
END;

IF NOT EXISTS (SELECT 1 FROM dbo.coaches WHERE full_name = N'Долгачев Александр')
BEGIN
INSERT INTO dbo.coaches (full_name, phone)
VALUES (N'Долгачев Александр', N'+70000000002');
END;

-- =========================
-- CLASS TYPES
-- (в V1 есть title, description, duration_min)
-- =========================
IF NOT EXISTS (SELECT 1 FROM dbo.class_types WHERE title = N'Тяжелая атлетика')
BEGIN
INSERT INTO dbo.class_types (title, description, duration_min)
VALUES (N'Тяжелая атлетика', NULL, 60);
END;

IF NOT EXISTS (SELECT 1 FROM dbo.class_types WHERE title = N'Кроссфит')
BEGIN
INSERT INTO dbo.class_types (title, description, duration_min)
VALUES (N'Кроссфит', NULL, 60);
END;

IF NOT EXISTS (SELECT 1 FROM dbo.class_types WHERE title = N'ОФП')
BEGIN
INSERT INTO dbo.class_types (title, description, duration_min)
VALUES (N'ОФП', NULL, 45);
END;

-- =========================
-- IDs по именам
-- =========================
DECLARE @room BIGINT = (SELECT id FROM dbo.rooms WHERE name = N'Большой зал');

DECLARE @coach1 BIGINT = (SELECT id FROM dbo.coaches WHERE full_name = N'Ригерт Владислав');
DECLARE @coach2 BIGINT = (SELECT id FROM dbo.coaches WHERE full_name = N'Долгачев Александр');

DECLARE @wt  BIGINT = (SELECT id FROM dbo.class_types WHERE title = N'Тяжелая атлетика');
DECLARE @cf  BIGINT = (SELECT id FROM dbo.class_types WHERE title = N'Кроссфит');
DECLARE @ofp BIGINT = (SELECT id FROM dbo.class_types WHERE title = N'ОФП');

-- =========================
-- CLASS SESSIONS (2×3) в одном зале
-- Фиксированные даты
-- =========================

-- Coach 1
IF NOT EXISTS (SELECT 1 FROM dbo.class_sessions WHERE start_at = '2025-12-08T10:00:00+00:00')
    INSERT INTO dbo.class_sessions (class_type_id, room_id, coach_id, start_at, capacity)
    VALUES (@wt,  @room, @coach1, '2025-12-08T10:00:00+00:00', 20);

IF NOT EXISTS (SELECT 1 FROM dbo.class_sessions WHERE start_at = '2025-12-08T12:00:00+00:00')
    INSERT INTO dbo.class_sessions (class_type_id, room_id, coach_id, start_at, capacity)
    VALUES (@cf,  @room, @coach1, '2025-12-08T12:00:00+00:00', 20);

IF NOT EXISTS (SELECT 1 FROM dbo.class_sessions WHERE start_at = '2025-12-08T18:00:00+00:00')
    INSERT INTO dbo.class_sessions (class_type_id, room_id, coach_id, start_at, capacity)
    VALUES (@ofp, @room, @coach1, '2025-12-08T18:00:00+00:00', 20);

-- Coach 2
IF NOT EXISTS (SELECT 1 FROM dbo.class_sessions WHERE start_at = '2025-12-08T11:00:00+00:00')
    INSERT INTO dbo.class_sessions (class_type_id, room_id, coach_id, start_at, capacity)
    VALUES (@wt,  @room, @coach2, '2025-12-08T11:00:00+00:00', 20);

IF NOT EXISTS (SELECT 1 FROM dbo.class_sessions WHERE start_at = '2025-12-08T15:00:00+00:00')
    INSERT INTO dbo.class_sessions (class_type_id, room_id, coach_id, start_at, capacity)
    VALUES (@cf,  @room, @coach2, '2025-12-08T15:00:00+00:00', 20);

IF NOT EXISTS (SELECT 1 FROM dbo.class_sessions WHERE start_at = '2025-12-08T19:00:00+00:00')
    INSERT INTO dbo.class_sessions (class_type_id, room_id, coach_id, start_at, capacity)
    VALUES (@ofp, @room, @coach2, '2025-12-08T19:00:00+00:00', 20);
