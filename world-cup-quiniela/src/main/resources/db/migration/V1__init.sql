create table quiniela_user (
    id              uuid primary key,
    display_name    varchar(50) not null,
    email           varchar(255) not null,
    password_hash   varchar(255) not null,
    created_at      timestamp not null default now(),
    updated_at      timestamp not null default now()
);

create unique index ux_quiniela_user_email
    on quiniela_user (email);