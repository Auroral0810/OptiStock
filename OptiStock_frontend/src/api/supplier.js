import request from '@/utils/request'


//获取供应商列表
export function getSupplierList(pageNum, pageSize, filterForm) {
    return request({
        url: `/supplier/getSupplierList`,
        method: 'post',
        data: {
            pageNum, 
            pageSize, 
            filterForm
        }
    })
}
//新增供应商
export function addSupplier(supplierForm) {
    return request({
        url: `/supplier/addSupplier`,
        method: 'put',
        data: supplierForm
    })
}
//编辑供应商
export function updateSupplier(supplierForm) {
    return request({
        url: `/supplier/updateSupplier`,
        method: 'patch',
        data: supplierForm
    })
}
//删除供应商
export function deleteSupplier(id) {
    return request({
        url: `/supplier/deleteSupplier?id=${id}`,
        method: 'delete'
    })
}