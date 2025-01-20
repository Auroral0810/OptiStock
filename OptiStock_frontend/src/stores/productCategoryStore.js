import { defineStore } from "pinia";

export const useProductCategoryStore = defineStore("productCategory", {
    state: () => ({
        CategoryList: [],
        skuList: [],
        categoryNameList: []
    }),
    actions: {
        setCategoryList(CategoryList) {
            this.CategoryList = CategoryList;
        },
        //删除分类的时候删除parentCategoryList
        deleteCategoryList(id) {
            this.CategoryList = this.CategoryList.filter(item => item.id !== id);
        },
        getCategoryList() {
            return this.CategoryList;
        },
        getSkuList() {
            return this.skuList;
        },
        getCategoryNameList() {
            return this.categoryNameList;
        },
        setSkuList(skuList) {
            this.skuList = skuList;
        },
        setCategoryNameList(categoryNameList) {
            this.categoryNameList = categoryNameList;
        },
        deleteSkuList(name) {
            this.skuList = this.skuList.filter(item => item.name !== name);
        },deleteCategoryNameList(name) {
            this.categoryNameList = this.categoryNameList.filter(item => item.name !== name);
        }
    },
    persist: true, // 开启持久化（VueUse）
});
