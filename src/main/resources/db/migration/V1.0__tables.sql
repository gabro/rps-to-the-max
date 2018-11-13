create extension if not exists "uuid-ossp";

create type move as enum ('Rock', 'Paper', 'Scissors');
create type result as enum ('UserWin', 'CpuWin', 'Draw');

create table game (
  id uuid not null,
  cpuMove move not null,
  userMove move not null,
  result result not null,
  createdAt timestamp not null default now(),
  primary key (id)
);
