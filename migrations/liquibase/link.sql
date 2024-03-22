create table if not exists link
(
    link_id   bigint generated always as identity,
    url       varchar(2083)            not null,
    update_at timestamp with time zone not null,
    check_at  timestamp with time zone not null,
    link_type text                     not null,
    data      text                     not null,

    primary key (link_id),
    unique (url)
);
