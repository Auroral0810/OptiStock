import request from '@/utils/request'

//获取库存列表数据
export function getStockList(pageNum, pageSize, filterForm) {
    return request({
        url: '/stock/getStockList',
        method: 'post',
        data: {
            pageNum,
            pageSize,
            filterForm
        }
    })
}
//库存调整
export function adjustStock(adjustForm) {
    return request({
        url: '/stock/adjustStock',
        method: 'put',
        data: adjustForm
    })
}
//获取库存调整记录
export function getStockAdjustRecord(id,timeRange) {
    return request({
        url: '/stock/getStockAdjustRecord',
        method: 'get',
        params: {id,timeRange}
    })
}
//获取阈值数据
export function getThresholdDataList(pageNum, pageSize, filterForm) {
    return request({
        url: '/stock/getThresholdDataList',
        method: 'post',
        data: {pageNum, pageSize, filterForm}
    })
}
//更新阈值
export function updateThreshold(id,newThreshold) {
    return request({
        url: '/stock/updateThreshold',
        method: 'patch',
        data: {
            id,
            newThreshold
        }
    })
}