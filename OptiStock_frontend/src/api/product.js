import request from '@/utils/request'

//获取全部的商品分类信息post方法，传入的是页数和每页数量
export function getAllProductCategory(pageNum, pageSize) {
    return request({
        url: '/product/getAllProductCategory',
        method: 'post',
        data: {
            pageNum: pageNum,
            pageSize: pageSize
        }
    })
}

//获取所有上级分类信息
export function getParentCategoryList() {
    return request({
        url: '/product/getParentCategoty',
        method: 'get'
    })
}

//新增分类
export function addCategory(name,parentId) {
    return request({
        url: '/product/addCategory',
        method: 'post',
        data: {
            name: name,
            parentId: parentId
        }
    })
}
// 更新分类
export function updateCategoryInfo(id,name,parentId) {
    return request({
        url: '/product/updateCategoryInfo',
        method: 'patch',
        data: {
            id: id,
            name: name,
            parentId: parentId
        }
    })
}

// 删除分类，使用delete方法，格式为http://localhost:9092/product/deleteCategory?id=9
export function deleteProductCategory(id) {
    return request({
        url: `/product/deleteCategory?id=${id}`,
        method: 'delete'
    })
}