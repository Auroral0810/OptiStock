create table if not exists `order`
(
    id            bigint auto_increment comment '订单唯一标识符'
        primary key,
    order_number  varchar(50)                                                                                 not null comment '订单编号',
    customer_name varchar(100)                                                                                not null comment '客户姓名',
    platform      varchar(50)                                                                                 not null comment '客户下单平台（如淘宝、拼多多等）',
    phone         varchar(20)                                                                                 not null comment '客户联系电话',
    address       varchar(255)                                                                                not null comment '客户收货地址',
    total_amount  decimal(10, 2)                                                                              not null comment '订单总金额',
    status        enum ('待付款', '已付款', '待发货', '已发货', '已完成', '已取消') default '待付款'          not null comment '订单状态',
    created_at    timestamp                                                         default CURRENT_TIMESTAMP null comment '订单创建时间',
    constraint order_number
        unique (order_number)
)
    comment '订单信息表';

