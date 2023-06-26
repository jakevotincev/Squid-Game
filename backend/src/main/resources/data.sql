insert into account (id, role) values (1, 'CHIEF') on conflict do nothing;
insert into account (id, role) values (2, 'MANAGER') on conflict do nothing;