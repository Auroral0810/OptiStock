create table if not exists return_orders
(
    id            bigint auto_increment comment '退货订单唯一标识符'
        primary key,
    order_id      bigint                                                    not null comment '关联的订单ID',
    product_id    bigint                                                    not null comment '退货商品ID',
    return_reason text                                                      null comment '退货原因',
    return_status enum ('待处理', '同意', '拒绝') default '待处理'          null comment '退货处理状态',
    reject_reason text                                                      null comment '拒绝申请原因（仅当退货被拒绝时填写）',
    return_date   timestamp                       default CURRENT_TIMESTAMP null comment '退货时间'
)
    comment '退货订单表';

