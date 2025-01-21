import { defineStore } from "pinia";

export const useStockStore = defineStore("stockList", {
    state: () => ({
        stockList: []
    }),
    actions: {
        setStockList(stockList) {
            this.stockList = stockList;
        },
        deleteStockList(id) {
            this.stockList = this.stockList.filter(item => item.id !== id);
        },
        getStockList() {
            return this.stockList;
        },
    },
    persist: true, // 开启持久化（VueUse）
});
