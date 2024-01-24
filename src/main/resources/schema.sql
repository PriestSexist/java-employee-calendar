create table if not exists role
(
    id   serial
        constraint role_pk
            primary key,
    name varchar(32) not null
);

create table if not exists users
(
    id                         serial
        constraint usr_pk
            primary key,
    email                      varchar(256)         not null
        constraint users_pk
            unique,
    password                   varchar(16)          not null,
    role_id                    integer              not null
        constraint usr_role_id_fk
            references role
            on update cascade on delete cascade,
    is_account_non_expired     boolean default true not null,
    is_account_non_locked      boolean default true not null,
    is_credentials_non_expired boolean default true not null,
    is_enabled                 boolean default true not null
);

create table if not exists not_working_employee
(
    id          serial
        constraint not_working_employee_pk
            primary key,
    employee_id integer      not null
        constraint not_working_employee_users_id_fk
            references users
            on update cascade on delete cascade,
    start_time  timestamp    not null,
    end_time    timestamp    not null,
    description varchar(512) not null
);

create table if not exists event
(
    id             serial
        constraint event_pk
            primary key,
    name           varchar(256)  not null,
    description    varchar(2048) not null,
    start_time     timestamp     not null,
    end_time       timestamp     not null,
    responsible_id integer       not null
        constraint event_users_id_fk
            references users
);