<!--
 * @Author: super-xxz 1070431833@qq.com
 * @Date: 2024-04-08 22:45:36
 * @LastEditors: super-xxz 1070431833@qq.com
 * @LastEditTime: 2024-04-09 23:48:14
 * @FilePath: \emrnew\src\components\PatientAside.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
	<el-row class="tac">
	  <el-col :span="12">
	    <el-menu
		  router="true"
		  default-active="2"
	      class="el-menu-vertical-demo">
	      <el-menu-item index="/myrecord" :class="$route.path=='/myrecord'?'active':''">
	        <i class="el-icon-document" :class="$route.path=='/myrecord'?'active':''"></i>
	        <span slot="title">我的病历</span>
	      </el-menu-item>
		  <el-submenu index="/1">
		          <template slot="title">
		            <i class="el-icon-s-check"></i>
		            <span>授权管理</span>
		          </template>
		          <el-menu-item-group>
					<el-menu-item index="/uploadrecord" :class="$route.path=='/uploadrecord'?'active':''">病历新增申请</el-menu-item>
		            <el-menu-item index="/managepermission" :class="$route.path=='/managepermission'?'active':''">病历查看申请</el-menu-item>
		          </el-menu-item-group>
		   </el-submenu>
	      <el-menu-item index="/patientcenter" :class="$route.path=='/patientcenter'?'active':''">
	        <i class="el-icon-setting" :class="$route.path=='/patientcenter'?'active':''"></i>
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