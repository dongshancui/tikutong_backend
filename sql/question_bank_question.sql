
create table if not exists tikutong.`question_bank_question`
(
    `id`            bigint not null auto_increment comment '默认id' primary key,
    `questionBankId` bigint not null comment '题库Id',
    `questionId`    bigint not null comment '题目Id',
    `userId`        bigint not null comment '创建用户id',
    `editTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '编辑时间',
    `createTime`    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`      tinyint default 0 not null comment '是否删除',
    unique (questionBankId,questionId)
    ) comment '题目表，用于存储题目具体信息';