<!--系统数据-->
<template>
  <div class="user-center-wrapper clear-fix">
    <el-card shadow="always" :body-style="{padding: '50px'}">
      <span>
		  
		  <el-form>
			  <el-form-item label="个人用户数量：">
					{{ userNumber.patientNumber }}
			  </el-form-item>
			  <el-form-item label="医生用户数量：">
					{{ userNumber.doctorNumber }}
			  </el-form-item>
			  <el-form-item label="有效资质码数量：">
			  		{{ userNumber.checkingNumber }}
			  </el-form-item>
		  </el-form>
		  
      </span>
    </el-card>
  </div>
</template>

<script>

export default {
  name: 'System',
  data() {
    return {
		userNumber:{
			patientNumber:'',
			doctorNumber:'',
			checkingNumber:'',
		},
	}
  },
  created() {
	this.getUserNumbers();
  },
  methods:{
	  getUserNumbers(){
		  axios.get('http://localhost:8181/manager/getusernumber')
		  .then(response => {
			console.log(response)
			if(response.data.code == 0){
				this.userNumber = response.data.data;
			}
			else{
				_this.$alert('请求失败，请稍后再试','提示',{
					confirmButtenText:'确定'
				})
			}
		})
	  }
  }
}
</script>

<style lang="less">
.user-center-wrapper {
  .user-avatar {
    float: left;
    width: 150px;
    height: 150px;
  }
  .user-info {
    float: left;
    width: 800px;
    margin-left: 50px;
    margin-bottom: 50px;
    li {
      height: 34px;
      line-height: 34px;
      label,
      span {
        display: inline-block;
        vertical-align: middle;
      }
      label {
        width: 80px;
        margin-right: 12px;
        text-align: right;
      }
    }
  }
}
// modify
.el-card {
 background: linear-gradient(to right, #87c0bd, #9ee9af)
}
</style>
