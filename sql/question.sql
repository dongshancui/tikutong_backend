
create table if not exists tikutong.`question`
(
    `id`            bigint not null auto_increment comment '默认id' primary key,
    `title`         varchar(256) default 'user' null comment '标题',
    `content`       text null comment '描述',
    `tags`          varchar(1024) null comment '标签列表（json数组）',
    `answer`        text null comment '推荐答案',
    `userId`        bigint not null comment '创建用户id',
    `editTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '编辑时间',
    `createTime`    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`      tinyint default 0 not null comment '是否删除',
    index idx_title(title),
    index idx_userId(userId)
    ) comment '题目表，用于存储题目具体信息';