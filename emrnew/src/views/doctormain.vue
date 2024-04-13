<template>
	<div>
		<el-container>
		  <el-header>
			  <DoctorHeader />
		  </el-header>
		  <el-container>
		    <el-aside>
				<DoctorAside />
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
	import DoctorAside from '../components/DoctorAside.vue'
	import DoctorHeader from '../components/DoctorHeader.vue'
	export default{
		data(){
			return{}
		},
		components:{
			DoctorAside,
			DoctorHeader
		}
	}
</script>

<style>
	.el-main{
		height:90vh;
	}
</style>