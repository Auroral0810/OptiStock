create table if not exists product
(
    id                bigint auto_increment comment '商品唯一标识符'
        primary key,
    name              varchar(255)                                            not null comment '商品名称',
    category_id       bigint                                                  not null comment '商品分类ID，关联product_category表',
    sku               varchar(50)                                             not null comment '库存单位编号（SKU）',
    price             decimal(10, 2)                                          not null comment '商品售价',
    cost_price        decimal(10, 2)                                          not null comment '商品成本价',
    stock_quantity    int                           default 0                 null comment '库存数量',
    unit              varchar(20)                   default '件'              null comment '计量单位，如件、盒、瓶等',
    status            enum ('上架', '下架', '停产') default '上架'            null comment '商品状态',
    created_at        timestamp                     default CURRENT_TIMESTAMP null comment '商品创建时间',
    updated_at        timestamp                     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '商品信息更新时间',
    warning_threshold decimal(10, 2)                default 0.00              null comment '库存预警阈值',
    constraint sku
        unique (sku)
)
    comment '商品信息表';

