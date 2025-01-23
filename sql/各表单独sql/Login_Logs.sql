create table if not exists Login_Logs
(
    id         bigint unsigned auto_increment comment '日志唯一标识'
        primary key,
    user_id    bigint unsigned                    not null comment '用户ID',
    login_time datetime default CURRENT_TIMESTAMP null comment '登录时间',
    ip_address varchar(45)                        not null comment '登录IP地址',
    device     varchar(255)                       null comment '登录设备信息',
    status     enum ('success', 'failed')         not null comment '登录是否成功',
    constraint login_logs_ibfk_1
        foreign key (user_id) references optistock.Users (id)
            on delete cascade
)
    comment '用户登录日志表';

create index user_id
    on Login_Logs (user_id);

