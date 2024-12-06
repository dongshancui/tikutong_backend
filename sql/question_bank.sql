
create table if not exists tikutong.`question_bank`
(
    `id`            bigint not null auto_increment comment '默认id' primary key,
    `title`         varchar(256) default 'user' null comment '标题',
    `description`   text null comment '描述',
    `picture`       varchar(2048) null comment '图片',
    `userId`        bigint not null comment '创建用户id',
    `editTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '编辑时间',
    `createTime`    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`      tinyint default 0 not null comment '是否删除',
    index idx_title(title)
    ) comment '题库表，用于存储用户创建的题库';