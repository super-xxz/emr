<template>
	<div class="login-wrapper">

		<div class="form-box">
			<div class="form-box-left" :class="{ 'form-box-left': true, 'transformed': moved }">
				<div class="guide-register-box">
					<div class="max-welcome-title">欢迎回来</div>
					<div class="min-welcome-title">与我们保持联系</div>
					<div class="login-btn" @click="toggleAnimation">{{ moved ? '登录' : '注册' }}</div>
				</div>

			</div>
			<div class="form-box-right" :class="{ 'form-box-right': true, 'transformed': moved }">

				<div class="guide-login-box" v-if="!moved">

					<div class="login-title">登录</div>
					<div class="form-box-right-content">
						<el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="0px"
							class="login-form">
							<el-form-item prop="phone">
								<el-input class="custom-radius-input" v-model="loginForm.phone" type="text" auto-complete="off" placeholder="请输入手机号"
									prefix-icon="el-icon-user" />
							</el-form-item>
							<el-form-item prop="password">
								<el-input class="custom-radius-input" v-model="loginForm.password" type="password" auto-complete="off"
									placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin" />
							</el-form-item>



							<div style="display: flex; align-items: center;">
								<el-input class="custom-radius-input" style="width: 180px; margin-right: 10px;" type="text" v-model="inputCode"
									placeholder="请输入验证码" />
								<div @click="refreshCode()" style="cursor: pointer; margin-left: 50px;">
									<SecurityCode :identifyCode="identifyCode"></SecurityCode>
								</div>
							</div>

                            <el-form-item style="margin-top: 20px;">
                                    <el-radio v-model="type" label="doctor">医生用户</el-radio>
                                    <el-radio v-model="type" label="patient" style="position: relative;left:80px">个人用户</el-radio>
                                    </el-form-item>





							<el-form-item style="margin-top: 50px;">
								<div style="display: flex; justify-content: center;">

									<el-button :loading="loading" round type="primary"
										@click.native.prevent="handleLogin">
										<span v-if="!loading">登 录</span>
										<span v-else>登 录 中...</span>
									</el-button>
								</div>
							</el-form-item>

						</el-form>
					</div>
				</div>

				<div class="create-an-account" v-if="moved && !(userReg || docReg)">
					<div class="create-an-account-title">创建账户</div>
					<div class="create-an-account-item" @click="() => userReg = true">个人用户注册</div>
					<div class="create-an-account-item" @click="() => docReg = true">医生用户注册</div>
				</div>


				<div class="create-an-account" v-if="userReg">
					<div class="create-an-account-title">个人用户注册</div>

					<el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
						<el-row>
							<el-col :span="24">
								<el-form-item prop="name" label="姓名：" label-width="80px">
									<el-input class="custom-radius-input" v-model="registerForm.name" type="text" auto-complete="off"
										placeholder="请输入姓名" />
								</el-form-item>
							</el-col>
							<el-col :span="24">
								<el-form-item prop="phone" label="电话：" label-width="80px">
									<el-input class="custom-radius-input" v-model="registerForm.phone" type="text" auto-complete="off"
										placeholder="请输入手机号" />
								</el-form-item>
							</el-col>
							<el-col :span="24">
								<el-form-item prop="password" label="密码：" label-width="80px">
									<el-input class="custom-radius-input" v-model="registerForm.password" type="password" auto-complete="off"
										placeholder="请输入密码" />
								</el-form-item>
							</el-col>
						</el-row>
						<el-form-item label-width="80px" prop="date" label="生日：">
							<el-date-picker v-model="registerForm.date" type="date" :picker-options="pickerOptions"
								placeholder="请选择出生日期"></el-date-picker>
						</el-form-item>
						<el-form-item label-width="80px" prop="sex" label="性别：">
							<template>
								<el-radio v-model="registerForm.sex" label="2">男</el-radio>
								<el-radio v-model="registerForm.sex" label="1">女</el-radio>
							</template>
						</el-form-item>

						<el-form-item>
							<div style="display: flex; justify-content: center;">
								<el-button :loading="loading" round type="primary"
									@click.native.prevent="patientRegister">
									<span v-if="!loading">注册</span>
									<span v-else>注册中...</span>
								</el-button>
							</div>
						</el-form-item>



					</el-form>
				</div>



				<div class="create-an-account" v-if="docReg">
					<div class="create-an-account-title">医生用户注册</div>

					<el-form ref="registerDoctorForm" :model="registerDoctorForm" :rules="registerDoctorRules" label-width="0px"
						class="register-form">
						<el-form-item prop="name" label="姓名：" label-width="80px">
							<el-input class="custom-radius-input" v-model="registerDoctorForm.name" type="text" auto-complete="off" placeholder="请输入姓名" />
						</el-form-item>
						<el-form-item class="custom-radius-input" prop="phone" label="电话：" label-width="80px">
							<el-input class="custom-radius-input" v-model="registerDoctorForm.phone" type="text" auto-complete="off"
								placeholder="请输入手机号" />
						</el-form-item>
						<el-form-item class="custom-radius-input" prop="password" label="密码：" label-width="80px">
							<el-input class="custom-radius-input" v-model="registerDoctorForm.password" type="password" auto-complete="off"
								placeholder="请输入密码" />
						</el-form-item>
						<el-form-item prop="hospital" label="医院：" label-width="80px">
							<el-input class="custom-radius-input" v-model="registerDoctorForm.hospital" type="hospital" auto-complete="off"
								placeholder="请输入医院" />
						</el-form-item>
						<el-form-item prop="office" label="科室：" label-width="80px">
							<el-input class="custom-radius-input" v-model="registerDoctorForm.office" type="office" auto-complete="off"
								placeholder="请输入科室" />
						</el-form-item>
						<el-form-item prop="checking" label="资质码：" label-width="80px">
							<el-input class="custom-radius-input" v-model="registerDoctorForm.checking" type="text" auto-complete="off"
								placeholder="请输入资质码" />
						</el-form-item>


						<el-form-item>
							<div style="display: flex; justify-content: center;">
								<el-button :loading="loading" round type="primary"
									@click.native.prevent="doctorRegister">
									<span v-if="!loading">注册</span>
									<span v-else>注册中...</span>
								</el-button>
							</div>
						</el-form-item>

					</el-form>
				</div>



			</div>
		</div>

<div v-if="visiblePatient">
		  <el-dialog
			title="提示"
			:visible.sync="visiblePatient"
			width="500px"
			:dangerously-use-html-string="true"
			@close="handleDialogClose"
		  >
			<div style="max-height: 400px; overflow-y: auto;">
			  注册成功，请牢记您的私钥：<br/><br/>
			  {{ privateKey }}<br/><br/>
			  <el-button type="primary" @click="copyPrivateKey" style="margin-top: 20px;">一键复制私钥</el-button>
			</div>
		  </el-dialog>
		</div>


        <div v-if="visibleDoctor">
		  <el-dialog
			title="提示"
			:visible.sync="visibleDoctor"
			width="500px"
			:dangerously-use-html-string="true"
			@close="handleDialogClose"
		  >
			<div style="max-height: 400px; overflow-y: auto;">
			  注册成功，请牢记您的私钥：<br/><br/>
			  {{ privateKeyDoctor }}<br/><br/>
			  <el-button type="primary" @click="copyprivateKeyDoctor" style="margin-top: 20px;">一键复制私钥</el-button>
			</div>
		  </el-dialog>
		</div>




	</div>
</template>

<script>
import Background from '../assets/login-background.jpg'
import Clipboard from 'clipboard';
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
			type: 'doctor',
			loginRules: {
				phone: [{ required: true, trigger: 'blur', message: '电话不能为空' }],
				password: [{ required: true, trigger: 'blur', message: '密码不能为空' }]
			},
			loading: false,
			form: [],
			dialogTableVisible2: false,
			moved: false,



			pickerOptions: {
				disabledDate(time) {
					return time.getTime() > Date.now();
				},
			},
			registerForm: {
				name: '',
				phone: '',
				password: '',
				date: '',
				sex: '',
			},

			registerRules: {
				name: [{ required: true, trigger: 'blur', message: '姓名不能为空' }],
				phone: [{ required: true, trigger: 'blur', message: '手机号不能为空' }],
				password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
				date: [{ required: true, trigger: 'change', message: '请选择出生日期' }],
				sex: [{ required: true, trigger: 'change', message: '请选择性别' }],
			},
			privateKey: '',
			visiblePatient: false,


			registerDoctorForm: {
				name: '',
				phone: '',
				password: '',
				hospital: '',
				office: '',
				checking: '',
			},
			registerDoctorRules: {
				name: [{ required: true, trigger: 'blur', message: '姓名不能为空' }],
				phone: [{ required: true, trigger: 'blur', message: '手机号不能为空' }],
				password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
				hospital: [{ required: true, trigger: 'blur', message: '医院不能为空' }],
				office: [{ required: true, trigger: 'blur', message: '科室不能为空' }],
				checking: [{ required: true, trigger: 'blur', message: '资质码不能为空' }],
			},
			privateKeyDoctor: '',
			visibleDoctor: false,
			userReg: false,
			docReg: false
		}


	},

	methods: {
        doctorRegister() {
            const _this = this
			console.log(this.registerDoctorForm)
			_this.$refs.registerDoctorForm.validate((valid) => {
                console.log('医生用户注册valid====>',valid);
				if (valid) {
					_this.loading = true;
					axios.post('http://localhost:8181/doctor/doctorregister', _this.registerDoctorForm)
						.then(function (resp) {
							console.log(resp);
							_this.loading = false;
							if (resp.data.code == -1) {
								_this.$alert('用户已存在，请登录', '提示', {
									confirmButtenText: '确定'
								})
								// _this.$router.replace({ path: '/login' })
							}
							if (resp.data.code == -2) {
								_this.$alert('注册失败，请稍后再试', '提示', {
									confirmButtenText: '确定'
								})
							}
							if (resp.data.code == -3) {
								_this.$alert('资质码验证失败，请检查信息是否有误', '提示', {
									confirmButtenText: '确定'
								})
							}
							if (resp.data.code === 0) {
								_this.privateKeyDoctor = resp.data.data
								_this.visibleDoctor = true
							}
						})
				}
			});
		},

		copyprivateKeyDoctor() {
			// 创建 Clipboard 实例
			const clipboard = new Clipboard('.el-button', {
				text: () => this.privateKeyDoctor // 设置复制的内容
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
		patientRegister() {
            const _this = this
			console.log(this.registerForm)
			_this.$refs.registerForm.validate((valid) => {
                console.log('个人用户注册====>',valid);
				if (valid) {
					_this.loading = true;
					axios.post('http://localhost:8181/patient/patientregister', _this.registerForm)
						.then(function (resp) {
							console.log(resp);
							_this.loading = false;
							if (resp.data.code == -1) {
								_this.$alert('用户已存在，请登录', '提示', {
									confirmButtenText: '确定'
								})
								_this.$router.replace({ path: '/login' })
							}
							if (resp.data.code == -2) {
								_this.$alert('注册失败，请稍后再试', '提示', {
									confirmButtenText: '确定'
								})
							}
							if (resp.data.code === 0) {
								_this.privateKey = resp.data.data
								_this.visiblePatient = true
							}
						})
				}
			});
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
        
        handleDialogClose(done) {
            this.toggleAnimation()
			// this.$router.push({ path: '/login' }); // 跳转到登录页面
			done(); // 必须调用 done()，否则弹窗将无法关闭
		},
		toggleAnimation() {
			this.moved = !this.moved;
			if(this.moved){
				this.userReg = false,
				this.docReg =  false

			}
		},
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
			}
			this.$refs.loginForm.validate((valid) => {
				if (valid) {
					this.loading = true
					let _this = this
					if (_this.type == 'doctor') {
						axios.get('http://localhost:8181/doctor/login', { params: _this.loginForm }).then(function (resp) {
							//console.log(resp)
							_this.loading = false
							if (resp.data.code == -1) {
								_this.$alert('用户不存在', '提示', {
									confirmButtenText: '确定'
								})
							}
							if (resp.data.code == -2) {
								_this.$alert('密码错误', '提示', {
									confirmButtenText: '确定'
								})
							}
							if (resp.data.code == 0) {
								localStorage.setItem('doctor', JSON.stringify(resp.data.data));
								_this.$router.replace({ path: '/doctor' })
							}
						})
					}
					if (_this.type == 'patient') {
						axios.get('http://localhost:8181/patient/login', { params: _this.loginForm }).then(function (resp) {
							//console.log(resp)
							_this.loading = false
							if (resp.data.code == -1) {
								_this.$alert('用户不存在', '提示', {
									confirmButtenText: '确定'
								})
							}
							if (resp.data.code == -2) {
								_this.$alert('密码错误', '提示', {
									confirmButtenText: '确定'
								})
							}
							if (resp.data.code == 0) {
								localStorage.setItem('patient', JSON.stringify(resp.data.data));
								_this.$router.replace({ path: '/patient' })
							}
						})
					}
				}
			});
		}
	},

	mounted() {
		this.refreshCode();
	},
}
</script>

<style lang="less" scoped>
.el-input_inner {
	border: 1px solid #fff !important;
}

.el-button.is-round {
	padding: 10px 50px !important;


}

.el-button {
	padding: 10px 50px !important;

}

.el-button--primary {
	background-color: #669092;
	border-color: #669092;
}

.el-button--primary:hover {
	border-color: #669092;
	background-color: #669092;
	opacity: .75;
}

.el-form-item {
	margin-bottom: 20px !important;
}



.login-wrapper {
	width: 100vw;
	height: 100vh;
	background: linear-gradient(to right, #ceffeb, #ede3dc);
  @keyframes float-up-down {

    //浮动
    0% {
      transform: translateY(0);
    }

    50% {
      transform: translateY(-20px);
    }

    100% {
      transform: translateY(0);
    }
  }
	.form-box {
		padding: 0 !important;
		margin: 0 auto;
		border-radius: 10px !important;
		overflow: hidden !important;
		height: 520px;
		width: 800px;
		position: relative;
		overflow: hidden;
		background-color: transparent;

    animation-name: float-up-down; //浮动
    animation-duration: 6s;
    animation-iteration-count: infinite;
    animation-direction: alternate;

    .custom-radius-input .el-input__inner {
      border-radius: 30px;
    }




    .form-box-left {



			background: linear-gradient(to bottom right, #679290, #93e6a7);



			position: absolute;
			transition: transform 0.5s ease;
			transform: translateX(0);

			.guide-register-box {
				display: flex;
				flex-direction: column;
				justify-content: space-evenly;
				align-items: center;
				width: 300px;
				height: 520px;

				>div {
					text-align: center;
					color: #fff;
					width: 200px;
				}

				.max-welcome-title {
					font-size: 30px;
					font-weight: bold;
				}

				.login-btn {
					line-height: 60px;
					height: 60px;
					border-radius: 50px;
					border: 2px solid #fff;
					font-weight: bold;
					cursor: pointer;
				}
			}



		}

		.form-box-right {

			background: linear-gradient(to right, #f0fff5, #f7f8f1) !important;

			position: absolute;
			right: 0;
			transition: transform 0.5s ease;
			transform: translateX(0);

			.guide-login-box {
				padding: 0 50px;
				height: 520px;
				width: 400px;
				display: flex;
				flex-direction: column;
				justify-content: space-evenly;


				.login-title {
					text-align: center;
					font-size: 30px;
					color: #678f8d;
					font-weight: bold;
				}

			}

			.create-an-account {
				padding: 0 50px;
				height: 520px;
				width: 400px;
				display: flex;
				flex-direction: column;
				justify-content: space-evenly;


				.create-an-account-title {
					text-align: center;
					font-size: 30px;
					color: #678f8d;
					font-weight: bold;
				}

				.create-an-account-item {
					cursor: pointer;
					border-radius: 30px;
					line-height: 60px;
					height: 60px;
					width: 100%;
					text-align: center;
					color: #fff;
					background-color: #669092;
				}



			}




		}


		.form-box-left.transformed {
			transform: translateX(500px);
		}

		.form-box-right.transformed {
			transform: translateX(-300px);
		}

	}

}
</style>
