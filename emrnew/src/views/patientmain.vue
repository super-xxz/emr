<template>
	<div>
		<el-container>
		  <el-header>
			  <PatientHeader />
		  </el-header>
		  <el-container>
		    <el-aside>
				<PatientAside />
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
	import PatientAside from '../components/PatientAside.vue'
	import PatientHeader from '../components/PatientHeader.vue'
	export default{
		data(){
			return{}
		},
		components:{
			PatientAside,
			PatientHeader
		}
	}
</script>

<style>
	.el-main{
		height:90vh;
	}
</style>