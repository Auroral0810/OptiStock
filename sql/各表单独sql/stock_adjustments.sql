create table if not exists stock_adjustments
(
    id              bigint auto_increment comment '库存调整唯一标识符'
        primary key,
    product_id      bigint                              not null comment '调整商品ID',
    adjustment_type enum ('损坏', '过期', '其他')       not null comment '库存调整类型',
    quantity        int                                 not null comment '调整数量',
    remarks         text                                null comment '调整备注',
    created_at      timestamp default CURRENT_TIMESTAMP null comment '调整时间'
)
    comment '库存调整记录表';

