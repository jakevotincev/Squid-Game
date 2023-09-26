-- insert into account (id, role) values (1, 'GLAVNIY') on conflict do nothing;
-- insert into account (id, role) values (2, 'MANAGER') on conflict do nothing;
insert into game (id) values (1) on conflict do nothing;
insert into account (id, name, role) values (3, 'Дедух', 'PLAYER') on conflict do nothing;
insert into account (id, name, role) values (4, 'Грудыгло', 'PLAYER') on conflict do nothing;
