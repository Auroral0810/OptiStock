create table if not exists purchase_items
(
    id                bigint auto_increment comment '采购商品唯一标识符'
        primary key,
    purchase_order_id bigint                              not null comment '采购订单ID',
    product_id        bigint                              not null comment '商品ID',
    quantity          int                                 not null comment '采购数量',
    cost_price        decimal(10, 2)                      not null comment '采购成本单价',
    total_price       decimal(10, 2)                      not null comment '采购总价格',
    created_at        timestamp default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '采购商品详情表';

