insert into account (id, name, role, participates_in_game)
values (1, 'glavniy', 'GLAVNIY', true)
on conflict do nothing;
insert into game (id)
values (1)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (2, 'manager', 'MANAGER', true)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (3, 'Дедух', 'PLAYER', false)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (4, 'Грудыгло', 'PLAYER', false)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (7, 'Мышь', 'PLAYER', false)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (8, 'Чичеринда', 'PLAYER', false)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (5, 'vasya', 'WORKER', true)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (6, 'leha', 'WORKER', true)
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id)
values (1, 'Как называется еврейский Новый год?', '{"Хнука":false,"Йом Кипур":false,"Кванза":false,"Рош ха-Шана":true}', 1, 1)
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id)
values (2, 'Сколько синих полос на флаге США?', '{"0":true,"3":false,"6":false,"13":false}', 1, 1)
on conflict do nothing;

insert into quiz (id, question, answers, game_id, round_id)
values (3, 'Кто из этих персонажей не дружит с Гарри Поттером?', '{"Драко Малфой":true,"Рон Уизли":false,"Гурмиона Грейнджер":false,"Невил Долгопупс":false}', 1, 1)
on conflict do nothing;

insert into account (id, name, role, participates_in_game)
values (10, 'jeka', 'SOLDIER', true)
on conflict do nothing;

