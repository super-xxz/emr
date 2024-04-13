<template>
	<div>
		<h3 class="text">医疗记录管理系统</h3>
		<el-dropdown>
			<span class="el-dropdown-link home_userinfo">
				{{patient.name}}<i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
			</span>
			<el-dropdown-menu slot="dropdown">
				<el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
			</el-dropdown-menu>
		</el-dropdown>
	</div>
</template>

<script>
	export default {
	  name: 'PatientHeader',
	  data() {
	    return {
	      patient: ''
	    }
	  },
	  created() {
	    let patient = JSON.parse(window.localStorage.getItem('patient'))
		this.patient = patient
	  },
	  methods:{
		  logout(){
			  let _this = this;
			  this.$confirm('注销登录吗?', '提示', {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning'
			  }).then(function () {
			      localStorage.removeItem('patient')
			      _this.$router.replace({path: '/login'})
			  })
		  }
	  }
	}
</script>

<style lang="less">
	.el-header{
		background-color: #146192;
	}
	.text{
		color: white;
	}
	.el-dropdown {
	 margin-bottom: 0px;
	 bottom: 40px;
	 left: 1140px;
	 font-size: 17px;
	 font-weight: bold;
	}

	.home_userinfo {
	    color: #fff;
	    cursor: pointer;
	}
</style>
