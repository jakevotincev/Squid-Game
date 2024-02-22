delete
from account;

insert into account (id, username, password, role, participates_in_game)
values (1, 'glavniy', '$2a$10$xLs.zzlixfDdXyrsRvjccumHrmrKaHgtNHplLeYtEuhpCzbIRq9Te', 'GLAVNIY', true)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (2, 'manager', '$2a$10$CjvYK9TcvteTGVxiHP8WTeWQbxBpU2F.g6RMXICn.NpBOD6NxPOTS', 'MANAGER', true)
on conflict do nothing;

insert into game (id)
values (1)
on conflict do nothing;

-- create with roles

-- insert into account (id, username, password, role, participates_in_game)
-- values (3, 'Дедух', '$2a$10$e4RX/eA.G9GYCX.tiXWe0uNw7pTHXx7Phf2GbLSYykGSRHEJuYQO.', 'PLAYER', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (4, 'Грудыгло', '$2a$10$3ZWZNrLKiVZe6WCxArCm8.fjL.D3InTi14KOc63QGCpgb/2R3JDjW', 'PLAYER', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (7, 'Мышь', '$2a$10$YGYY1KVe5WzPTrvv3WP2huIllI1pUU/nb9QfVzn6N9rZ7NjQcVHcm', 'PLAYER', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (8, 'Чичеринда', '$2a$10$U2ynS/TwY7kW1T4a/5vjM.VadDIkJPNiBb4MgS/CCuiu70Lyw1LS2', 'PLAYER', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (5, 'vasya', '$2a$10$8.mWqm3X2mOcDAFYRaykI.xBUCUSYeFBLH5z9TP.6UhUhBhix7k8m', 'WORKER', true)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (6, 'leha', '$2a$10$8i36HTXkfGmMLnwx0ELS9eMqUwQPgcMCOxFF6aMR5b9h1quzdmAnq', 'WORKER', true)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (10, 'jeka', '$2a$10$.RDBcDtAPTtC/B2JNJuMouB7naiKb6OUqgFY9ALnIpQD3FiuiaHFC', 'SOLDIER', true)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (11, 'dima', '$2a$10$XATdzwtYfxFLBbqfjPjwAuCC4yTioYwytrC8WgD6YNQEVgbAecHzy', 'SOLDIER', true)
-- on conflict do nothing;

-- create without roles

insert into account (id, username, password, role, participates_in_game)
values (3, 'Дедух', '$2a$10$e4RX/eA.G9GYCX.tiXWe0uNw7pTHXx7Phf2GbLSYykGSRHEJuYQO.', 'UNDEFINED', false)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (4, 'Грудыгло', '$2a$10$3ZWZNrLKiVZe6WCxArCm8.fjL.D3InTi14KOc63QGCpgb/2R3JDjW', 'UNDEFINED', false)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (7, 'Мышь', '$2a$10$YGYY1KVe5WzPTrvv3WP2huIllI1pUU/nb9QfVzn6N9rZ7NjQcVHcm', 'UNDEFINED', false)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (8, 'Чичеринда', '$2a$10$U2ynS/TwY7kW1T4a/5vjM.VadDIkJPNiBb4MgS/CCuiu70Lyw1LS2', 'UNDEFINED', false)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (5, 'vasya', '$2a$10$8.mWqm3X2mOcDAFYRaykI.xBUCUSYeFBLH5z9TP.6UhUhBhix7k8m', 'UNDEFINED', false)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (6, 'leha', '$2a$10$8i36HTXkfGmMLnwx0ELS9eMqUwQPgcMCOxFF6aMR5b9h1quzdmAnq', 'UNDEFINED', false)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (10, 'jeka', '$2a$10$.RDBcDtAPTtC/B2JNJuMouB7naiKb6OUqgFY9ALnIpQD3FiuiaHFC', 'UNDEFINED', false)
on conflict do nothing;

insert into account (id, username, password, role, participates_in_game)
values (11, 'dima', '$2a$10$XATdzwtYfxFLBbqfjPjwAuCC4yTioYwytrC8WgD6YNQEVgbAecHzy', 'UNDEFINED', false)
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id)
values (1, 'Как называется еврейский Новый год?',
        '{"Ханука":false,"Йом Кипур":false,"Кванза":false,"Рош ха-Шана":true}', 1, 1)
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id)
values (2, 'Сколько синих полос на флаге США?', '{"0":true,"3":false,"6":false,"13":false}', 1, 1)
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id)
values (3, 'Кто из этих персонажей не дружит с Гарри Поттером?',
        '{"Драко Малфой":true,"Рон Уизли":false,"Гермиона Грейнджер":false,"Невил Долгопупс":false}', 1, 1)
on conflict do nothing;

delete
from criteria;
delete
from form;


