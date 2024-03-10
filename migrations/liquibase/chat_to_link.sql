create table if not exists chat_link
(
    chat_id bigint not null references chat (chat_id),
    link_id bigint not null references link (link_id),

    constraint id primary key (chat_id, link_id)
);
