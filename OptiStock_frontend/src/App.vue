<script setup>
import { onMounted } from 'vue'

// 不需要直接导入Login和Register组件,因为它们已经在路由配置中导入了

// 添加百度统计代码
onMounted(() => {
  var _hmt = _hmt || [];
  (function() {
    var hm = document.createElement("script");
    hm.src = "https://hm.baidu.com/hm.js?4471b7bbdf6f9c0bf76674ec404d9012";
    var s = document.getElementsByTagName("script")[0]; 
    s.parentNode.insertBefore(hm, s);
  })();
})
</script>

<template>
  <router-view></router-view>
</template>

<style scoped>

</style>
