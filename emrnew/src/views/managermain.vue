<template>
	<div>
		<el-container>
		  <el-header>
			  <ManagerHeader />
		  </el-header>
		  <el-container>
		    <el-aside>
				<ManagerAside />
			</el-aside>
			
		    <el-main>
				<!-- 子路由出口 -->
				<router-view></router-view>
			</el-main>
		  </el-container>
		</el-container>
	</div>
</template>

<script>
	import ManagerAside from '../components/ManagerAside.vue'
	import ManagerHeader from '../components/ManagerHeader.vue'
	export default{
		data(){
			return{}
		},
		components:{
			ManagerAside,
			ManagerHeader
		}
	}
</script>

<style>
	.el-main{
		height:90vh;
	}
</style>