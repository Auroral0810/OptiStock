import request from '@/utils/request'

//获取订单列表
export const getOrderList = (pageNum, pageSize, filterForm) => {
  return request({
    url: "/order/getOrderList",
    method: "post",
    data: {
      pageNum,
      pageSize,
      filterForm
    }
  })
}

//删除订单
export const deleteOrder = (orderNumber) => {
  return request({
    url: `/order/deleteOrder?orderNumber=${orderNumber}`,
    method: "delete"
  })
}
//新增订单
export const addOrder = (data) => {
  return request({
    url: "/order/addOrder",
    method: "put",
    data
  })
}
//修改订单（只能修改订单的收货地址和收货人手机号、姓名、状态，状态只能修改从待发货变成已发货，改变之后需要出库，即减少对应的库存）
export const updateOrder = (data) => {
  return request({
    url: `/order/updateOrder`,
    method: "patch",
    data
  })
}
//订单发货
export const changeOrderStatus = (orderNumber) => {
  return request({
    url: `/order/changeOrderStatus?orderNumber=${orderNumber}`,
    method: "patch"
  })
}
//获取商品名称列表
export const getProductName = () => {
  return request({
    url: "/order/getProductName",
    method: "get"
  })
}