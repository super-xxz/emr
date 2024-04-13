<!--
 * @Author: super-xxz 1070431833@qq.com
 * @Date: 2024-04-08 22:45:36
 * @LastEditors: super-xxz 1070431833@qq.com
 * @LastEditTime: 2024-04-13 15:28:08
 * @FilePath: \emrnew\src\App.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<style lang="less">
html,body{
  margin: 0;
  padding: 0;
  background: linear-gradient(to right, #ceffeb, #ede3dc);
}

.el-header{
  background: linear-gradient(to right, #679290, #93e6a7) !important;


}


.el-button--primary {
  background-color: #669092 !important;
  border-color: #669092 !important;
}

.el-button--primary:hover {
  border-color: #669092 !important;
  background-color: #669092 !important;
  opacity: .75;
}

.el-pagination.is-background .el-pager li:not(.disabled).active{
  border-color: #669092 !important;
  background-color: #669092 !important;
}

.el-radio__input.is-checked ::v-deep + .el-radio__label {
  color: #669092 !important;
}

.el-radio__input.is-checked ::v-deep .el-radio__inner {
  background-color:#669092 !important;
  border-color: #669092 !important;
}



</style>
