create table if not exists order_items
(
    id          bigint auto_increment comment '订单商品唯一标识符'
        primary key,
    order_id    bigint                              not null comment '关联的订单ID',
    product_id  bigint                              not null comment '商品ID',
    quantity    int                                 not null comment '商品数量',
    price       decimal(10, 2)                      not null comment '商品单价',
    total_price decimal(10, 2)                      not null comment '商品总价',
    created_at  timestamp default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '订单商品详情表';

