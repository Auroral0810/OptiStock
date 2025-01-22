import {defineStore} from "pinia"

export const usePurchaseStore = defineStore("purchaseOrder",{
    state:()=>({
        purchaseOrderList:[]
    }),
    actions:{
        setPurchaseOrderList(purchaseOrderList){
            this.purchaseOrderList = purchaseOrderList
        },
        deletePurchaseOrderList(id){
            this.purchaseOrderList = this.purchaseOrderList.filter(item=>item.id !== id)
        },
        getPurchaseOrderList(){
            return this.purchaseOrderList
        }
    },
    persist:true
})