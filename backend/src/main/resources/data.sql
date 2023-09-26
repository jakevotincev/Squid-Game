-- insert into account (id, role) values (1, 'GLAVNIY') on conflict do nothing;
insert into account (id, name, role)
values (2, 'manager', 'MANAGER')
on conflict do nothing;

insert into game (id)
values (1)
on conflict do nothing;

insert into account (id, name, role)
values (3, 'Дедух', 'PLAYER')
on conflict do nothing;

insert into account (id, name, role)
values (4, 'Грудыгло', 'PLAYER')
on conflict do nothing;

insert into account (id, name, role)
values (7, 'Мышь', 'PLAYER')
on conflict do nothing;

insert into account (id, name, role)
values (8, 'Чичеринда', 'PLAYER')
on conflict do nothing;

insert into account (id, name, role)
values (5, 'vasya', 'WORKER')
on conflict do nothing;

insert into account (id, name, role)
values (6, 'leha', 'WORKER')
on conflict do nothing;
