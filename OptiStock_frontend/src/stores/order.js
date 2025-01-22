import {defineStore} from "pinia"

export const useOrderStore = defineStore("order",{
    state:()=>({
        orderList:[]
    }),
    actions:{
        setOrderList(orderList){
            this.orderList = orderList
        },
        deleteOrderList(id){
            this.orderList = this.orderList.filter(item=>item.id !== id)
        },
        getOrderList(){
            return this.orderList
        }
    },
    persist:true
})