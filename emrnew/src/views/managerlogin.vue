<template>
  <div class="login-wrapper">
    <div class="form-box" >
      <div class="form-title">
        <p>系统管理员登录</p>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="0px" class="login-form">
        <el-form-item prop="phone">
          <el-input class="custom-radius-input" v-model="loginForm.phone" type="text" auto-complete="off" placeholder="请输入手机号" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input class="custom-radius-input" v-model="loginForm.password" type="password" auto-complete="off" placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin" />
        </el-form-item>
		<div style="display: flex; align-items: center;">
		  <el-input
		    class="custom-radius-input"
		    style="width: 180px; margin-right: 10px;"
		    type="text"
		    v-model="inputCode"
		    placeholder="请输入验证码"
		  />
		  <div @click="refreshCode()" style="cursor: pointer; margin-left: 50px;">
		    <!--验证码组件-->
		    <SecurityCode :identifyCode="identifyCode"></SecurityCode>
		  </div>
		</div>
        <el-form-item>
          <el-button :loading="loading" size="small" type="primary" class="custom-radius" style="width:100%; margin-top: 30px;" @click.native.prevent="handleLogin"><!--修改边框-->
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>	
	
  </div>
</template>

<script>
import Background from '../assets/login-background.jpg'
import SecurityCode from "@/components/securityCode.vue";

export default {
  components: { SecurityCode },
  name: 'Login',
  data() {
    return {
      Background,
	  //验证码
	  identifyCodeType: "1234567890", //定义验证类型 1.数字 2.字母
	  identifyCode: "",
	  inputCode: "", //text框输入的验证码
      loginForm: {
        phone: '',
        password: '',
      },
      loginRules: {
        phone: [{ required: true, trigger: 'blur', message: '电话不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }]
      },
      loading: false,
	  form:[],
	  dialogTableVisible2: false
	}
  },
  
  methods: {
	//验证码
	randomNum(min, max) {
		return Math.floor(Math.random() * (max - min) + min);
	},
	//初始化验证码
	refreshCode() {
	this.identifyCode = ""; //输入框置空
	this.makeCode(this.identifyCodeType, 4); //验证码长度为4
	},
	//随机切换验证码
	makeCode(o, l) {
	for (let i = 0; i < l; i++) {
	  this.identifyCode +=
		this.identifyCodeType[
		  this.randomNum(0, this.identifyCodeType.length)
		];
	}
	console.log(this.identifyCode);
	},
		  
    handleLogin() {
		if (this.inputCode == "") {
		  alert("请输入验证码");
		  return;
		}
		if (this.identifyCode !== this.inputCode) {
		  this.$message.error("验证码错误");
		  // 清空输入框内容
		  this.inputCode = "";
		  // 刷新验证码
		  this.refreshCode();
		  return;
		} //前端验证码验证，容易被绕过，一起提交给后端比较可靠
		this.$refs.loginForm.validate((valid) => {
		    if (valid) {
				this.loading =true
		        let _this = this
				axios.get('http://localhost:8181/manager/login',{params:_this.loginForm}).then(function (resp){
					//console.log(resp)
					_this.loading =false
					if(resp.data.code == -1){
						_this.$alert('账号密码错误，请重试','提示',{
							confirmButtenText:'确定'
						})
					}
					if(resp.data.code == 0){
						_this.$router.replace({path:'/manager'})
					}
				})
		    }
		});
    }
  },
  
	mounted() {
	  this.refreshCode();
	},
}
</script>

<style lang="less">
.table {
   width: 200px;
}
.login-wrapper {
    display: flex;
	justify-content: center;
    align-items: center;
    justify-content: center;
    width: 100vw;
    height: 100vh;
    background: linear-gradient(to right, #ceffeb, #ede3dc);
	@keyframes float-up-down {//浮动
  0% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
  100% { transform: translateY(0); }
}
  .form-box {
	background: linear-gradient(to right, #f0fff5, #f7f8f1) !important;//修改边框
	border-radius: 10px !important;
    width: 320px;
    padding: 15px 30px 20px;
    background: #f4f4f4;
    border-radius: 4px;
    box-shadow: 0 15px 30px 0 rgba(0, 0, 1, .1);
	margin-left: -50%;

	animation-name: float-up-down;//浮动
    animation-duration: 6s; 
    animation-iteration-count: infinite;
    animation-direction: alternate;

	.custom-radius {                     //边框圆角
  border-radius: 50px !important;
}
  .custom-radius-input .el-input__inner {
  border-radius: 30px; /* 根据需求调整数值 */
}

    .form-title {
      margin: 0 auto 35px;
      text-align: center;
      color: #678f8d;
	  font-weight: bold;
      font-size: 18px;
      letter-spacing: 2px;
    }
  }
}
</style>
