create table if not exists purchase_order
(
    id          bigint auto_increment comment '采购订单唯一标识符'
        primary key,
    supplier_id bigint                                                                  not null comment '供应商ID',
    total_cost  decimal(10, 2)                                                          not null comment '采购总成本',
    status      enum ('待审核', '已通过', '已驳回', '已完成') default '待审核'          not null comment '采购订单状态',
    created_at  timestamp                                     default CURRENT_TIMESTAMP null comment '采购订单创建时间'
)
    comment '采购订单表';

