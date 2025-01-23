create table if not exists stock_in
(
    id          bigint auto_increment comment '入库记录唯一标识符'
        primary key,
    product_id  bigint                              not null comment '入库商品ID',
    supplier_id bigint                              null comment '供应商ID',
    quantity    int                                 not null comment '入库数量',
    cost_price  decimal(10, 2)                      not null comment '入库成本单价',
    total_cost  decimal(10, 2)                      not null comment '入库总成本',
    received_by bigint                              not null comment '经办人员ID',
    created_at  timestamp default CURRENT_TIMESTAMP null comment '入库时间'
)
    comment '库存入库记录表';

