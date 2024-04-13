import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/login.vue'
// import Loginres from '../views/loginres.vue'
import Register from '../views/Register.vue'
import PatientRegister from '../views/patientRegister.vue'
import DoctorRegister from '../views/doctorRegister.vue'

import Doctormain from '../views/doctormain.vue'
import CreateRecord from '../views/createrecord.vue'
import SearchRecord from '../views/searchrecord.vue'
import ViewRecord from '../views/viewrecord.vue'
import DoctorCenter from '../views/doctorcenter.vue'

import Patientmain from '../views/patientmain.vue'
import MyRecord from '../views/myrecord.vue'
import UploadRecord from '../views/uploadrecord.vue'
import ManagePermission from '../views/managepermission.vue'
import PatientCenter from '../views/patientcenter.vue'

import ManagerLogin from '../views/managerlogin.vue'
import ManagerMain from '../views/managermain.vue'
import System from '../views/system.vue'
import AddChecking from '../views/addchecking.vue'
import ManageChecking from '../views/managechecking.vue'

Vue.use(VueRouter)

const routes = [

  {
	  path: '/login',
	  name: '登录',
	  component: Login
  },
//   {
//      path:'/loginres',
// 	 component:Loginres
//   },
  {
  	path:'/register',
  	component:Register,
  },
  {
  	path:'/patientregister',
  	component:PatientRegister,
  },
  {
  	path:'/doctorregister',
  	component:DoctorRegister,
  },
  {
  	path:'/doctor',
  	component:Doctormain,
  	redirect:'/createrecord',
  	children:[
  		//子路由
  		{path:'/createrecord',component:CreateRecord}, //新建病历
  		{path:'/searchrecord',component:SearchRecord}, //检索病历
  		{path:'/viewrecord',component:ViewRecord}, //查看病历
  		{path:'/doctorCenter',component:DoctorCenter} //个人中心
  	]
  },
  
  {
  	path:'/patient',
  	component:Patientmain,
  	redirect:'/myrecord',
  	children:[
  		//子路由
  		{path:'/myrecord',component:MyRecord}, //查看病历
		{path:'/uploadrecord',component:UploadRecord}, //新增病历管理
  		{path:'/managepermission',component:ManagePermission}, //查看病历管理
  		{path:'/patientcenter',component:PatientCenter} //个人中心
  	]
  },
  {
  	path:'/managerlogin',
  	component:ManagerLogin,
  },
  {
  	path:'/manager',
  	component:ManagerMain,
  	redirect:'/system',
  	children:[
  		//子路由
  		{path:'/system',component:System}, //系统数据概览
  		{path:'/addchecking',component:AddChecking}, //新增资质
  		{path:'/managechecking',component:ManageChecking}, //资质管理
  	]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
