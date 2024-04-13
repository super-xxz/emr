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
  color: #669092 !important;/* 你想要的颜色 */;
}

.el-radio__input.is-checked ::v-deep .el-radio__inner {
  background-color:#669092 !important; /* 你想要的颜色 */;
  border-color: #669092 !important;/* 你想要的颜色 */;
}
    
    

</style>

