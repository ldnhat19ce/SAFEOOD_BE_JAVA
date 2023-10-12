create table if not exists user_favorite_review
(
    id int auto_increment primary key,
    user_id bigint not null,
    review_id bigint not null,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    modified_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    CONSTRAINT `fk_user_favorite_review_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_user_favorite_review_2` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`)
) charset = utf8;

create table if not exists user_favorite_reply
(
    id int auto_increment primary key,
    user_id bigint not null,
    reply_id bigint not null,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    modified_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    CONSTRAINT `fk_user_favorite_reply_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_user_favorite_reply_2` FOREIGN KEY (`reply_id`) REFERENCES `reply` (`id`)
) charset = utf8;