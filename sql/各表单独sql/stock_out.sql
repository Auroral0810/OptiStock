create table if not exists stock_out
(
    id         bigint auto_increment comment '出库记录唯一标识符'
        primary key,
    order_id   bigint                              null comment '关联的订单ID',
    product_id bigint                              not null comment '出库商品ID',
    quantity   int                                 not null comment '出库数量',
    shipped_by bigint                              not null comment '经办人员ID',
    created_at timestamp default CURRENT_TIMESTAMP null comment '出库时间'
)
    comment '库存出库记录表';

