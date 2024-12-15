create database tikutong;
       use tikutong;


-- 用于存储用户信息，根据用户密码登录，逻辑删除
create table if not exists tikutong.`user`
(
    `id` bigint not null auto_increment comment '默认id' primary key,
    `userAccount` varchar(256) not null comment '用户账号',
    `userPassword` varchar(512) not null comment '用户密码',
    `unionId` varchar(256) null comment '微信开放平台id',
    `mpOpenId` varchar(256) null comment '公众号openId',
    `username` varchar(256) null comment '用户名',
    `userAvatar` varchar(1024) null comment '用户头像',
    `userProfile` varchar(512) null comment '用户简介',
    `userRole` varchar(256) default 'user' not null comment '用户角色，user/admin',
    `editTime` datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除',
    index idx_unionId(unionId)
    ) comment '用于存储用户信息，根据用户密码登录，逻辑删除';

