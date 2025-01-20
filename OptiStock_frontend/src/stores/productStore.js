import { defineStore } from "pinia";

export const useProductStore = defineStore("productList", {
    state: () => ({
        productList: []
    }),
    actions: {
        setProductList(productList) {
            this.productList = productList;
        },
        //删除分类的时候删除parentCategoryList
        deleteProductList(id) {
            this.productList = this.productList.filter(item => item.id !== id);
        },
        getProductList() {
            return this.productList;
        },
    },
    persist: true, // 开启持久化（VueUse）
});
