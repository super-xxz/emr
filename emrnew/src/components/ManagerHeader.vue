<template>
	<div class="container">
		<h3 class="text">医疗记录管理系统</h3>
		<el-button class="logout-btn" type="text" style="color: white;" @click.native="logout">退出登录</el-button>
	</div>
</template>

<script>
	export default {
	  name: 'ManagerHeader',
	  data() {
	    return {
	    }
	  },
	  created(){
	  },
	  methods:{
		  logout(){
		      let _this = this;
		      this.$confirm('注销登录吗?', '提示', {
		          confirmButtonText: '确定',
		          cancelButtonText: '取消',
		          type: 'warning'
		      }).then(function () {
		          _this.$router.replace({path: '/managerlogin'})
		      }).catch(function () {
		          // 处理取消操作
		      });
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
	.container {
	  display: flex;
	  justify-content: space-between;
	  align-items: center;
	}

	.logout-btn {
	  margin-left: auto;
	}
	.home_userinfo {
	    color: #fff;
	    cursor: pointer;
	}
</style>
