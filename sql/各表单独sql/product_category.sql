create table if not exists product_category
(
    id         bigint auto_increment comment '分类唯一标识符'
        primary key,
    name       varchar(100)                        not null comment '分类名称',
    parent_id  bigint                              null comment '父分类ID，顶级分类为NULL',
    created_at timestamp default CURRENT_TIMESTAMP null comment '分类创建时间',
    constraint name
        unique (name)
)
    comment '商品分类表，存储商品的层级分类';

