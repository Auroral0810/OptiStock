import request from '@/utils/request'

//获取订单列表
export const getOrderList = (params) => {
  return request({
    url: "/order/list",
    method: "get",
    params
  })
}
//删除订单
export const deleteOrder = (id) => {
  return request({
    url: `/order/delete?id=${id}`,
    method: "delete"
  })
}