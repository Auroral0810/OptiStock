create table if not exists supplier
(
    id         bigint auto_increment comment '供应商唯一标识符'
        primary key,
    name       varchar(255)                        not null comment '供应商名称',
    contact    varchar(100)                        null comment '联系人姓名',
    phone      varchar(20)                         null comment '联系电话',
    email      varchar(255)                        null comment '电子邮件',
    address    varchar(255)                        null comment '供应商地址',
    created_at timestamp default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '供应商信息表';

