import { defineStore } from "pinia";

export const useProductCategoryStore = defineStore("productCategory", {
    state: () => ({
        CategoryList: []
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
        }
    },

    persist: true, // 开启持久化（VueUse）
});
