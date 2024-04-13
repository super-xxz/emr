<template>
	<el-row class="tac">
	  <el-col :span="12">
	    <el-menu
		  router="true"
		  default-active="2"
	      class="el-menu-vertical-demo"
		  background-color="#f7f8fb">
		  
		  <el-menu-item index="/system" :class="$route.path=='/system'?'active':''" >
		    <i class="el-icon-s-home" :class="$route.path=='/system'?'active':''"></i>
		    <span slot="title">系统数据</span>
		  </el-menu-item>
	      <el-menu-item index="/addchecking" :class="$route.path=='/addchecking'?'active':''">
	        <i class="el-icon-circle-plus-outline" :class="$route.path=='/addchecking'?'active':''"></i>
	        <span slot="title">新增资质</span>
	      </el-menu-item>
		  <el-menu-item index="/managechecking" :class="$route.path=='/managechecking'?'active':''">
		    <i class="el-icon-setting" :class="$route.path=='/managechecking'?'active':''"></i>
		    <span slot="title">资质管理</span>
		  </el-menu-item>
		  
	    </el-menu>
	  </el-col>
	</el-row>
</template>

<script>
  export default {
    methods: {
    }
  }
</script>


<style lang="less">
	.el-aside{
		height: auto;
		width: 150px !important;
		.el-row{
			height: 100%;
		}
		.el-col-12{
			height: 100%;
			width: 100%;
		}
		.el-menu{
			height: 100%;
		}
		.active{
			color: #659191 !important;
		}
	}
</style>