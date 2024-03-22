create table if not exists chat
(
    chat_id    bigint                   not null,
    name       text                     not null,

    created_at timestamp with time zone not null,

    primary key (chat_id),
    unique (name)
);
