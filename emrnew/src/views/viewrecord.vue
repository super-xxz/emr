<template>
  <div class="table-classic-wrapper">
    <el-card shadow="always"
      <!-- 查询栏 -->
      <el-form
        ref="listQuery"
        :inline="true"
        :model="listQuery"
        label-width="90px"
        class="search-form"
      >
	    <el-form-item label="病历编号" prop="recordid" style="margin-left: 80px;">
	      <el-input v-model="listQuery.recordid" placeholder="病历编号" />
	    </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="listQuery.name" placeholder="姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="listQuery.phone" placeholder="电话" />
        </el-form-item>
		<el-form-item label="医院" prop="hospital" style="margin-left: 80px;">
		  <el-input v-model="listQuery.hospital" placeholder="医院" />
		</el-form-item>
        <el-form-item label="科室" prop="office">
          <el-input v-model="listQuery.office" placeholder="科室" />
        </el-form-item>
        <el-form-item>
			<el-button @click="resetForm('listQuery')" style="margin-left: 100px;">重置</el-button>
        </el-form-item>
		<el-form-item>
			<el-button type="primary" @click="onSubmit('listQuery')" style="margin-left: 10px;">查询</el-button>
		</el-form-item>
      </el-form>
	  
      <!-- 表格栏 -->
      <el-table
        v-loading="listLoading"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        size="medium"
      >
        <el-table-column prop="id" label="病历编号" align="center"/>
		<el-table-column prop="createtime" label="创建时间" align="center"sortable />
        <el-table-column prop="name" label="姓名" align="center"/>
        <el-table-column prop="phone" label="手机" align="center" />
        <el-table-column prop="sex" label="性别" align="center">
			<template slot-scope="scope">
				<p v-if="scope.row.sex==1">女</p>
				<p v-if="scope.row.sex==2">男</p>
			</template>
		</el-table-column>
		<el-table-column prop="hospital" label="就诊医院" align="center" />
        <el-table-column prop="office" label="就诊科室" align="center" />
        <el-table-column label="操作" align="center" width="200">
        	<template slot-scope='scope'>
        		<el-button size="mini"@click='view(scope.row)'>查看</el-button>
        	</template>
        </el-table-column>
      </el-table>
	  
		<!-- 分页栏 -->
		<el-pagination
			background
			layout="prev, pager, next"
			:page-size="pageSize"
			:total="total"
			:current-page.sync="currentPage"
			@current-change="page"
			style="text-align: right; margin-top: 20px;">
		</el-pagination>
	 
	  <!-- 输入私钥 第一层弹出栏 -->
      <el-col :span="12">
      		  <el-dialog title="输入私钥" :visible.sync="open" width="50%">
      			  <el-form ref="privatekey" :model="privatekey" label-width="100px" :inline="true">
      				  <el-input v-model="privatekey.key" type="text" auto-complete="off" placeholder="请输入私钥"></el-input>
      			  </el-form>
      			  <el-button type="primary" @click="inputkey('privatekey')" style="margin-top: 20px;">确定</el-button>
      		  </el-dialog>
      </el-col>
	  
	  <!-- 查看 弹出栏 -->
	  <el-col :span="12">
		  <el-dialog
			title="查看档案"
			:visible.sync="secondopen"
			width="50%"
		  >
			<el-form
			  ref="dialogForm"
			  :model="dialogForm"
			  label-width="100px"
			  :inline="true"
			>
			  <el-form-item label="就诊医院:">
				{{dialogForm.hospital}}
			  </el-form-item>
			  <el-form-item label="就诊科室:">
				{{dialogForm.office}}
			  </el-form-item>
			  <el-form-item label="病历创建时间:" style="margin-left: 30px;">
				{{dialogForm.createtime}}
			  </el-form-item>
			  <br />
			  <div style="margin-top: -20px;"><hr /></div>
			  <el-form-item label="姓名:">
				<label slot="label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
				{{dialogForm.name}}
			  </el-form-item>
			  <el-form-item label="联系电话:">
				{{dialogForm.phone}}
			  </el-form-item>
			  <el-form-item label="性别:">
				<label slot="label">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:</label>
				<div v-if="dialogForm.sex == 1">女</div>
				<div v-if="dialogForm.sex == 2">男</div>
			  </el-form-item>
			  <el-form-item label="年龄:">
				<label slot="label">年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;龄:</label>
				{{dialogForm.age}}
			  </el-form-item>
			  <br />
			  <el-form-item label="医生诊断:">
				{{dialogForm.description}}
			  </el-form-item>
			  <br />
			  <el-form-item label="备注">
				<label slot="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
				{{dialogForm.remark}}
			  </el-form-item>
			</el-form>
		</el-dialog>
	  </el-col>
    </el-card>
  </div>
</template>

<script>

export default {
  name: 'Table',
  data() {
    return {
      // 数据列表加载动画
      //listLoading: true,
      // 查询列表参数对象
      listQuery: {
        name: '',
        phone: '',
		recordid:'',
		hospital:'',
        office: '',
        page: '',
        size: 10,
		doctorid:'',
      },
	  //私钥输入框
	  privatekey:{
	  	key:'',
	  },
	  //后端解密所需数据
	  decryptForm:{
	  	privatekey:'',
	  	description:'',
	  	remark:'',
	  },
	  //当前页数
	  currentPage: 1,
	  //每页数据条数
	  pageSize: 10,
      // 数据总条数
      total: 0,
      // 表格数据数组
      tableData: [],
	  //弹出框数据数组
	  dialogForm: {
	    hospital: '',
	    office: '',
	    createtime: '',
	    name: '',
	    phone: '',
	    sex: '',
	    description: '',
	    remark: ''
	  },
	  //弹出框显示/隐藏
	  open:false,
	  secondopen: false,
    }
  },
  created() {
  	const _this = this
	let doctor = JSON.parse(window.localStorage.getItem('doctor'))
	_this.doctor = doctor
	_this.listQuery.page = 1
	_this.listQuery.doctorid = _this.doctor.id
	axios.get('http://localhost:8181/record/doctorview',{params:_this.listQuery}).then(function(resp){
		console.log(resp)
		_this.tableData = resp.data.data.data
		_this.total = resp.data.data.total 
	})
  },
  methods: {
	//页面切换
	page(currentPage){
		const _this = this
		_this.listQuery.page = _this.currentPage
		axios.get('http://localhost:8181/record/doctorview',{params:_this.listQuery}).then(function(resp){
			console.log(resp)
			_this.tableData = resp.data.data.data
			_this.total = resp.data.data.total
		})
	},
    // 查询数据
    onSubmit(formName) {
	  //翻页复原
	  this.currentPage = 1
	  this.$refs[formName].validate((valid) => {
		  if(valid){
			  const _this = this
			  let doctor = JSON.parse(window.localStorage.getItem('doctor'))
			  _this.doctor = doctor
			  _this.listQuery.page = _this.currentPage
			  _this.listQuery.doctorid = _this.doctor.id
			  _this.tableData = []
			  axios.get('http://localhost:8181/record/doctorview',{params:_this.listQuery}).then(function(resp){
				  _this.tableData = resp.data.data.data
				  _this.total = resp.data.data.total
			  })
		  }else{
			  alert('请添加查询条件!');
			  return false;
		  }
	  })
    },
	//重置查询
	resetForm(formName) {
	    this.$refs[formName].resetFields();
	},
	view(row){
		console.log(row)
		this.dialogForm.description = ''
		this.dialogForm.remark = ''
		this.open = true
		this.dialogForm.hospital = row.hospital
		this.dialogForm.office = row.office
		this.dialogForm.createtime = row.createtime
		this.dialogForm.name = row.name
		this.dialogForm.phone = row.phone
		this.dialogForm.sex = row.sex
		this.dialogForm.age = row.age
		this.decryptForm.description = row.description
		this.decryptForm.remark = row.remark
	},
	inputkey(formName){
		this.open = false
		this.$refs["privatekey"].validate((valid) => {
			  if(valid){
				  const _this = this
				  _this.decryptForm.privatekey = _this.privatekey.key
				  console.log(_this.decryptForm)
				  axios.get('http://localhost:8181/record/decrypt',{params:_this.decryptForm}).then(function(resp){
					  console.log(resp)
					  console.log(resp.data.data.success)
					  if(resp.data.data.success == false){
						  alert('解密失败，请检查私钥是否输入正确');
					  }else{
						  _this.dialogForm.description = resp.data.data.description
						  _this.dialogForm.remark = resp.data.data.remark
						  _this.secondopen = true
					  }
					  // 重置输入框的值
					  _this.privatekey.key = '';
				  })
				  
			  }else{
				  alert('请输入私钥!');
				  return false;
			  }
		})
	},
  }
}
</script>

<style lang="less">
.table-classic-wrapper {
  .el-card {
    min-height: 656px;
  }
  .search-form {
    padding-top: 18px;
    margin-bottom: 15px;
    background-color: #f7f8fb;
  }
  
  .dialog-form {
    .el-input {
      width: 380px;
    }
    .footer-item {
      margin-top: 50px;
      text-align: right;
    }
  }
}
</style>