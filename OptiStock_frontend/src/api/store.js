import request from '@/utils/request'

//获取库存列表数据
export function getStoreList(pageNum, pageSize, filterForm) {
    return request({
        url: '/store/getStoreList',
        method: 'post',
        data: filterForm
    })
}