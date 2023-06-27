-- insert into account (id, role) values (1, 'GLAVNIY') on conflict do nothing;
-- insert into account (id, role) values (2, 'MANAGER') on conflict do nothing;
insert into game (id) values (1) on conflict do nothing;