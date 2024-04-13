<template>
  <div class="register-wrapper" :style="'background-image:url('+ Background +')'">
    <div class="form-box">
      <div class="form-title">
        <p>个人用户注册</p>
      </div>
      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
        <el-row>
          <el-col :span="24">
            <el-form-item prop="name" label="姓名：" label-width="80px">
              <el-input v-model="registerForm.name" type="text" auto-complete="off" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="phone" label="电话：" label-width="80px">
              <el-input v-model="registerForm.phone" type="text" auto-complete="off" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="password" label="密码：" label-width="80px">
              <el-input v-model="registerForm.password" type="password" auto-complete="off" placeholder="请输入密码"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="80px" prop="date" label="生日：" >
          <el-date-picker v-model="registerForm.date" type="date" :picker-options="pickerOptions" placeholder="请选择出生日期"></el-date-picker>
        </el-form-item>
        <el-form-item label-width="80px" prop="sex" label="性别：">
          <template>
            <el-radio v-model="registerForm.sex" label="2">男</el-radio>
            <el-radio v-model="registerForm.sex" label="1">女</el-radio>
          </template>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" size="small" type="primary" style="width:100%;" @click.native.prevent="patientRegister">
            <span v-if="!loading">注册</span>
            <span v-else>注册中...</span>
          </el-button>
        </el-form-item>
        <div style="margin-top: 10px;">
          已有账号？<router-link to="/login">去登录</router-link>
        </div>
      </el-form>
    </div>
		
	<!-- 弹窗 -->
		<div v-if="visible">
		  <el-dialog
			title="提示"
			:visible.sync="visible"
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
  </div>
</template>

<script>
import Background from '../assets/login-background.jpg'
import Clipboard from 'clipboard';

export default {
  name: 'Register',
  data() {
    return {
      Background,
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
	  privateKey:'',
      loading: false,
	  visible:false,
    }
  },
  methods: {
    patientRegister() {
		
	  const _this = this
	  console.log(this.registerForm)
      _this.$refs.registerForm.validate((valid) => {
        if (valid) {
          _this.loading = true;
          axios.post('http://localhost:8181/patient/patientregister', _this.registerForm)
			.then(function(resp){
				console.log(resp);
				_this.loading = false;
				if(resp.data.code == -1){
					_this.$alert('用户已存在，请登录','提示',{
						confirmButtenText:'确定'
					})
					_this.$router.replace({path:'/login'})
				}
				if(resp.data.code == -2){
					_this.$alert('注册失败，请稍后再试','提示',{
						confirmButtenText:'确定'
					})
				}
				if (resp.data.code === 0) {
				    _this.privateKey = resp.data.data
					_this.visible = true
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
	        this.$router.push({ path: '/login' }); // 跳转到登录页面
	        done(); // 必须调用 done()，否则弹窗将无法关闭
	}
		
  },
}
</script>

<style lang="less">
.register-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background-size: cover;
  .form-box {
    width: 320px;
    padding: 15px 30px 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 15px 30px 0 rgba(0, 0, 1, .1);
    margin-left: -50%;
    .form-title {
      margin: 0 auto 35px;
      text-align: center;
      color: #707070;
      font-size: 18px;
      letter-spacing: 2px;
    }
  }
}
</style>
