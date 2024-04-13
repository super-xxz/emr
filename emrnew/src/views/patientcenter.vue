<template>
  <div class="user-center-wrapper clear-fix">
    <el-card shadow="always" :body-style="{padding: '50px'}">
      <span>
		  <el-form>
			  <el-form-item label="姓名：">
					{{ patient.name }}
			  </el-form-item>
			  <el-form-item label="电话：">
	 				{{ patient.phone }}
					<el-button size="mini" type="primary" style="margin-left: 30px;" @click="changephone()">修改</el-button>
			  </el-form-item>
			  <el-form-item label="性别：">
			  		<div v-if="patient.sex == 1">女</div>
			  		<div v-if="patient.sex == 2">男</div>
			  </el-form-item>
			  <el-form-item label="出生日期：">
			  		{{ patient.date }}
			  </el-form-item>
		  </el-form>
		  <el-button size="mini" type="primary" @click="changepassword()">修改密码</el-button>
		  <el-button size="mini" type="danger" @click="forgetkey()">忘记密钥</el-button>
		  
		  <!-- 修改电话 弹出栏 -->
		  <el-col :span="12">
			  <el-dialog
						title="修改电话"
						:visible.sync="formVisible1"
						width="50%"
			  >
				<el-form
				  ref="changePhoneForm"
				  :model="changePhoneForm"
				  label-width="100px"
				  :inline="true"
				>
					<el-row>
					  <el-col :span="24">
					    <el-form-item prop="phone">
					      <el-input v-model="changePhoneForm.phone" type="text" auto-complete="off" placeholder="请输入新电话" prefix-icon="el-icon-user" />
					    </el-form-item>
					  </el-col>
					</el-row>
				</el-form>
				<el-button type="primary" @click="updatephone('changeForm')">确定</el-button>
			  </el-dialog>
		  </el-col>
		  
		  <!-- 修改密码 弹出栏 -->
		  <el-col :span="12">
			  <el-dialog
						title="修改密码"
						:visible.sync="formVisible2"
						width="50%"
			  >
				<el-form
				  ref="changePasswordForm"
				  :model="changePasswordForm"
				  label-width="100px"
				  :inline="true"
				>
					<el-row>
					  <el-col :span="24">
						<el-form-item prop="oldpassword">
						  <el-input v-model="changePasswordForm.oldpassword" type="password" auto-complete="off" placeholder="请输入旧密码" prefix-icon="el-icon-lock" />
						</el-form-item>
					  </el-col>
					</el-row>
					<el-row>
					  <el-col :span="24">
						<el-form-item prop="newpassword">
						  <el-input v-model="changePasswordForm.newpassword" type="password" auto-complete="off" placeholder="请输入新密码" prefix-icon="el-icon-lock" />
						</el-form-item>
					  </el-col>
					</el-row>
				</el-form>
				<el-button type="primary" @click="updatepassword('changeForm')">确定</el-button>
			  </el-dialog>
		  </el-col>
		  
		  <!-- 忘记密钥 弹出栏 -->
		  <el-col :span="12">
			  <el-dialog
						title="重新生成密钥对"
						:visible.sync="formVisible3"
						width="50%"
			  >
				<div>您确定要重新生成密钥对吗？</div>
				<div>重新生成密钥对后，之前的全部病历将无法查看！</div>
				<el-button @click="createnewkey()" style="margin-top: 30px;">确定</el-button>
				<el-button type="danger" @click="closeDialog" style="margin-top: 30px;">取消</el-button>
			  </el-dialog>
		  </el-col>
		  
		  <!-- 生成新密钥 弹窗 -->
		  	<div v-if="formVisible4">
		  	  <el-dialog
		  		title="提示"
		  		:visible.sync="formVisible4"
		  		width="500px"
		  		:dangerously-use-html-string="true"
		  		@close="handleDialogClose"
		  	  >
		  		<div style="max-height: 400px; overflow-y: auto;">
		  		  重置成功，请牢记您的私钥：<br/><br/>
		  		  {{ privateKey }}<br/><br/>
		  		  <el-button type="primary" @click="copyPrivateKey" style="margin-top: 20px;">一键复制私钥</el-button>
		  		</div>
		  	  </el-dialog>
		  	</div>
      </span>
    </el-card>
  </div>
</template>

<script>
import Clipboard from 'clipboard';

export default {
  name: 'UserCenter',
  data() {
    return {
      patient: '',
	  privateKey:'',
	  changePhoneForm: {
	  	  id:'',
	      phone: '',
	  },
	  changePasswordForm: {
	  	  id:'',
	      oldpassword:'',
		  newpassword:'',
	  },
	  //弹出框显示/隐藏
	  formVisible1: false,
	  formVisible2: false,
	  formVisible3: false,
	  formVisible4: false,
	}
  },
  created() {
    let patient = JSON.parse(window.localStorage.getItem('patient'))
	this.patient = patient
  },
  methods:{
  	  changephone(){
  		  this.formVisible1 = true
  	  },
	  changepassword(){
	      this.formVisible2 = true
	  },
	  forgetkey(){
	  	  this.formVisible3 = true
	  },
	  closeDialog() {
		  // 关闭弹窗
		  this.formVisible3 = false;
	  },
  	  updatephone(formName){
  		  const _this = this
  		  if(_this.changePhoneForm.phone==""){
  			  alert('请填写完整信息')
  			  return false;
  		  }else{
  			  _this.changePhoneForm.id = _this.patient.id
  			  axios.put('http://localhost:8181/patient/updatephone',_this.changePhoneForm).then(function(resp){
  			  	if(resp.data.code == 0){
  			  		alert('修改成功！请重新登录！')
					_this.$router.replace({path:'/login'})
  			  	}else{
  			  		alert('修改失败！')
  			  	}
  			  })
  		  }
  	   },
	   updatepassword(formName){
		  const _this = this
		  if(_this.changePasswordForm.oldphone=="" || _this.changePasswordForm.newphone==""){
			  alert('请填写完整信息')
			  return false;
		  }else{
			  _this.changePasswordForm.id = _this.patient.id
			  axios.put('http://localhost:8181/patient/updatepassword',_this.changePasswordForm).then(function(resp){
				if(resp.data.code == 0){
					alert('修改成功！请重新登录！')
				_this.$router.replace({path:'/login'})
				}else{
					alert('修改失败！请检查后重试！')
				}
			  })
		  }
	    },
		createnewkey(){
			const _this = this
			axios.post('http://localhost:8181/patient/createnewkey/'+ _this.patient.id)
			.then(function(resp){
				if (resp.data.code === 0) {
					_this.formVisible3 = false
					_this.privateKey = resp.data.data
					_this.formVisible4 = true
				}else{
					alert('重置失败！请稍后重试！')
				}
			})
		},
		copyPrivateKey() {
		      // 创建 Clipboard 实例
		      const clipboard = new Clipboard('.el-button', {
		        text: () => this.privateKey // 设置复制的内容
		      });
		      // 复制成功后的回调函数
		      clipboard.on('success', e => {
		        this.$message({
		          message: '私钥已成功复制到剪贴板',
		          type: 'success'
		        });
		        // 手动解除 clipboard.js 内部绑定的事件监听器
		        clipboard.destroy();
		      });
		      // 复制失败后的回调函数
		      clipboard.on('error', e => {
		        this.$message.error('复制失败，请手动复制私钥');
		      });
		},
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
</style>
