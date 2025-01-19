import { defineStore } from "pinia";

export const useStore = defineStore("main", {
    state: () => ({
        user: null,
        token: ""
    }),
    actions: {
        setUser(userInfo, authToken) {
            this.user = userInfo;
            this.token = authToken;
        },
        logout() {
            this.user = null;
            this.token = "";
        }
    },
    persist: true, // 开启持久化（VueUse）
});
