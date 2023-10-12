create table if not exists notification
(
    id          bigint             auto_increment primary key,
    title       varchar(255)        not null,
    content     varchar(255)        not null,
    from_user_id     bigint                  null,
    to_user_id       bigint                  null,
    seen_flag   tinyint             not null  default '0',
    type VARCHAR(255) NULL,
    type_id int NULL,
    type_name text null,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    modified_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint fk_from_user_id
        foreign key (from_user_id) references user (id),
    constraint fk_to_user_id
        foreign key (to_user_id) references user (id)
) charset = utf8;