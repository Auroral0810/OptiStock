import request  from '@/utils/request'

export const getInventoryData = (params) => {
  return request({
    url: '/statistics/getInventoryData',
    method: 'post',
    data: params
  })
}

// 获取销售统计数据
export const getSalesStatistics = (params) => {
  // console.log('销售统计请求参数:', {
  //   startDate: params.startDate, // 开始日期,如: "2024-01-01"
  //   endDate: params.endDate // 结束日期,如: "2024-01-31" 
  // })
  return request({
    url: '/statistics/getSalesStatistics',
    method: 'post',
    data: params
  })
}
// 获取采购统计数据
export const getPurchaseStatistics = (params) => {
  return request({
    url: '/statistics/getPurchaseStatistics',
    method: 'post',
    data: params
  })
}