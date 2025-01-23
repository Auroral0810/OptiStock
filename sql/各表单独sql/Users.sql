create table if not exists Users
(
    id            bigint unsigned auto_increment comment '用户唯一标识'
        primary key,
    username      varchar(50)                          not null comment '用户名（唯一）',
    nickname      varchar(100)                         null comment '用户昵称（可选）',
    password_hash varchar(255)                         not null comment 'MD5加密后的密码',
    email         varchar(100)                         null comment '邮箱地址（可选）',
    phone         varchar(20)                          null comment '手机号（可选）',
    avatar        varchar(255)                         null comment '头像URL',
    role          tinyint(1) default 1                 not null comment '用户角色：0=管理员，1=普通用户',
    status        tinyint(1) default 1                 not null comment '账户状态：0=未激活，1=正常，2=封禁',
    created_at    timestamp  default CURRENT_TIMESTAMP null comment '账户创建时间',
    updated_at    timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    last_login    datetime                             null comment '最近一次登录时间',
    constraint email
        unique (email),
    constraint phone
        unique (phone),
    constraint username
        unique (username)
)
    comment '用户表';

