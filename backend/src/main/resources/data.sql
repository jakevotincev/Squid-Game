delete
from score;
delete
from form;
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
-- values (3, 'deduh', '$2a$10$e4RX/eA.G9GYCX.tiXWe0uNw7pTHXx7Phf2GbLSYykGSRHEJuYQO.', 'PLAYER', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (4, 'grudoglo', '$2a$10$3ZWZNrLKiVZe6WCxArCm8.fjL.D3InTi14KOc63QGCpgb/2R3JDjW', 'PLAYER', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (7, 'mice', '$2a$10$YGYY1KVe5WzPTrvv3WP2huIllI1pUU/nb9QfVzn6N9rZ7NjQcVHcm', 'PLAYER', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (8, 'chicherinda', '$2a$10$U2ynS/TwY7kW1T4a/5vjM.VadDIkJPNiBb4MgS/CCuiu70Lyw1LS2', 'PLAYER', false)
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

-- insert into account (id, username, password, role, participates_in_game)
-- values (3, 'deduh', '$2a$10$e4RX/eA.G9GYCX.tiXWe0uNw7pTHXx7Phf2GbLSYykGSRHEJuYQO.', 'UNDEFINED', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (4, 'grudoglo', '$2a$10$3ZWZNrLKiVZe6WCxArCm8.fjL.D3InTi14KOc63QGCpgb/2R3JDjW', 'UNDEFINED', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (7, 'mice', '$2a$10$YGYY1KVe5WzPTrvv3WP2huIllI1pUU/nb9QfVzn6N9rZ7NjQcVHcm', 'UNDEFINED', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (8, 'chicherinda', '$2a$10$U2ynS/TwY7kW1T4a/5vjM.VadDIkJPNiBb4MgS/CCuiu70Lyw1LS2', 'UNDEFINED', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (5, 'vasya', '$2a$10$8.mWqm3X2mOcDAFYRaykI.xBUCUSYeFBLH5z9TP.6UhUhBhix7k8m', 'UNDEFINED', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (6, 'leha', '$2a$10$8i36HTXkfGmMLnwx0ELS9eMqUwQPgcMCOxFF6aMR5b9h1quzdmAnq', 'UNDEFINED', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (10, 'jeka', '$2a$10$.RDBcDtAPTtC/B2JNJuMouB7naiKb6OUqgFY9ALnIpQD3FiuiaHFC', 'UNDEFINED', false)
-- on conflict do nothing;
--
-- insert into account (id, username, password, role, participates_in_game)
-- values (11, 'dima', '$2a$10$XATdzwtYfxFLBbqfjPjwAuCC4yTioYwytrC8WgD6YNQEVgbAecHzy', 'UNDEFINED', false)
-- on conflict do nothing;


insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (1, 'Как называется еврейский Новый год?',
        '{"Ханука":false,"Йом Кипур":false,"Кванза":false,"Рош ха-Шана":true, "Караганда":false, "Хачундра":false, ' ||
        '"Еврейка":false}', 1, 1, 'GAME_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (2, 'Сколько синих полос на флаге США?', '{"0":true,"3":false,"6":false,"13":false, "2":false, "52":false, ' ||
                                                '"31":false}', 1, 1, 'GAME_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (3, 'Кто из этих персонажей не дружит с Гарри Поттером?',
        '{"Драко Малфой":true,"Рон Уизли":false,"Гермиона Грейнджер":false,"Невил Долгопупс":false, "Луна Лавгуд":false,' ||
        '"Фред Уизли":false, "Джордж Уизли":false}', 1, 1, 'GAME_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (4,
        'Вам нужно приготовить блины. Для этого вам понадобится: молоко, мука, сахар, ?. Назовите недостающий ингредиент.',
        '{"Какао":false,"Сгущенное молоко":false,"Яйца":true,"Творог":false}', 1, 1, 'MAKE_FOOD_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (5, 'Вам нужно приготовить салат Оливье. Для этого вам понадобится: картофель, морковь, яйца, огурцы, ' ||
           'зеленый горошек, майонез, ?. Назовите недостающий ингредиент.',
        '{"Курица":false,"Свекла":false,"Кукуруза":false,"Колбаса":true}', 1, 1, 'MAKE_FOOD_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (6, 'Вам нужно приготовить борщ. Для этого вам понадобится: вода, говядина, свекла, морковь, лук, ' ||
           'картофель, лавровый лист, соль, ?. Назовите недостающий ингредиент.',
        '{"Капуста":true,"Огурец":false,"Перец чили":false,"Редька":false}', 1, 1, 'MAKE_FOOD_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (7, 'Блюдо содержит ингредиенты: вода, говядина, свекла, морковь, лук, ' ||
           'картофель, лавровый лист, соль, капуста. Назовите название блюда.',
        '{"Солянка":false,"Борщ":true,"Рассольник":false,"Харчо":false}', 1, 1, 'EAT_FOOD_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (8, 'Блюдо содержит ингредиенты: картофель, морковь, яйца, огурцы, ' ||
           'зеленый горошек, майонез, колбаса. Назовите название блюда.',
        '{"Оливье":true,"Крабовый":false,"Мимоза":false,"Витаминный":false}', 1, 1, 'EAT_FOOD_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (9, 'Блюдо содержит ингредиенты: молоко, мука, сахар, яйца. Назовите название блюда.',
        '{"Сдобное печенье":false,"Пирожное картошка":false,"Блины":true,"Медовик":false}', 1, 1, 'EAT_FOOD_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (10, '88:8=?',
        '{"11":true}', 1, 1, 'PREPARE_GAME_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (11, '560+256=?',
        '{"816":true}', 1, 1, 'PREPARE_GAME_QUIZ')
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id, quiz_type)
values (12, '190-74=?',
        '{"116":true}', 1, 1, 'PREPARE_GAME_QUIZ')
on conflict do nothing;

delete
from criteria;



