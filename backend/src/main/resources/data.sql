-- todo: разобраться почему главнй не добавляется
-- insert into account (id, name, role, participates_in_game)
-- values (1, 'glavniy', 'GLAVNIY', true)
-- on conflict do nothing;
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
