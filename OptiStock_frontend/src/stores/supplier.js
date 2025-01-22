import { defineStore } from "pinia";

export const useSupplierStore = defineStore("supplierList", {
    state: () => ({
        supplierList: []
    }),
    actions: {
        setSupplierList(supplierList) {
            this.supplierList = supplierList;
        },
        deleteSupplierList(id) {
            this.supplierList = this.supplierList.filter(item => item.id !== id);
        },
        getSupplierList() {
            return this.supplierList;
        },
        getSupplierByName(name) {
            return this.supplierList.find(item => item.name === name);
        }
    },
    persist: true, // 开启持久化（VueUse）
});
