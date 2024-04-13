<template>
	<el-row class="tac">
	  <el-col :span="12">
	    <el-menu
		  router="true"
		  default-active="2"
	      class="el-menu-vertical-demo"
		   background="linear-gradient(to right, #679290, #93e6a7)">
		  <el-submenu index="/1">
		          <template slot="title">
		            <i class="el-icon-document"></i>
		            <span>病历查看</span>
		          </template>
		          <el-menu-item-group>
						<el-menu-item index="/searchrecord" :class="$route.path=='/searchrecord'?'active':''">病历检索</el-menu-item>
						<el-menu-item index="/viewrecord" :class="$route.path=='/viewrecord'?'active':''">已申请病历</el-menu-item>
		          </el-menu-item-group>
		   </el-submenu>
		  <el-menu-item index="/createrecord" :class="$route.path=='/createrecord'?'active':''" >
		    <i class="el-icon-folder-add" :class="$route.path=='/createrecord'?'active':''"></i>
		    <span slot="title">新建病历</span>
		  </el-menu-item>
	      <el-menu-item index="/doctorcenter" :class="$route.path=='/doctorcenter'?'active':''">
	        <i class="el-icon-setting" :class="$route.path=='/doctorcenter'?'active':''"></i>
	        <span slot="title">个人中心</span>
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