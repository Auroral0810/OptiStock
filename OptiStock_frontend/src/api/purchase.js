import request from '@/utils/request'

//获取采购订单列表
export const getPurchaseOrderList = (pageNum,pageSize,filterForm) => {
  return request({
    url: "/purchase/getPurchaseOrderList",
    method: "post",
    data:{
      pageNum:pageNum,
      pageSize:pageSize,
      filterForm:filterForm
    }
  })
}
//删除采购订单
export const deletePurchaseOrder = (id) =>{
  return request({
    url: `/purchase/delete?id=${id}`,
    method: "delete"
  })
}
//新增采购订单
export const addPurchaseOrder = (data) =>{
  return request({
    url: "/purchase/addPurchaseOrder",
    method: "put",
    data
  })
}
//审核采购订单
export const auditPurchaseOrder = (id, status) => {
  return request({
    url: `/purchase/auditPurchaseOrder?id=${id}&status=${status}`,
    method: "patch"
  })
};

//获取供应商名称和商品名称
export const getSupplierAndProduct = () =>{
  return request({
    url: "/purchase/getSupplierAndProduct",
    method: "get"
  })
}