<template>
  <div class="register-wrapper" :style="'background-image:url('+ Background +')'">
    <div class="form-box" >
      <div class="form-title">
        <p>注 册</p>
      </div>
      <div style="display: flex; flex-direction: column; align-items: center;">
        <el-button type="primary" style="width:80%;" @click.native.prevent="topatient">
          个人用户注册
        </el-button>
	  </div>
	  <div>
        <el-button type="primary" style="width:80%; margin-top: 20px; margin-left: 32px;" @click.native.prevent="todoctor">
          医生用户注册
        </el-button>
      </div>
	  <div style="margin-top: 40px;">
	    已有账号？<a href="/login">去登录</a>
	  </div>
    </div>  
  </div>
</template>

<script>
import Background from '../assets/login-background.jpg'

export default {
  name: 'Register',
  data() {
    return {
      Background,
    }
  },
  methods: {
    topatient() {
      this.$router.push('/patientRegister');
    },
    todoctor() {
      this.$router.push('/doctorRegister');
    },
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
  background-size: 100% 100%;
}

.form-box {
  width: 320px;
  padding: 15px 30px 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 15px 30px 0 rgba(0, 0, 1, .1);
}

.form-title {
  margin: 0 auto 35px;
  text-align: center;
  color: #707070;
  font-size: 18px;
  letter-spacing: 2px;
}

</style>
