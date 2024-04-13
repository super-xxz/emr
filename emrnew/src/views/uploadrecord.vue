<template>
  <div class="table-classic-wrapper">
    <el-card shadow="always"
      <!-- 表格栏 -->
      <el-table
        ref="multipleTable"
        v-loading="listLoading"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        size="medium"
      >
        <el-table-column prop="id" label="病历编号" align="center"sortable />
		<el-table-column prop="createtime" label="创建时间" align="center"sortable />
        <el-table-column prop="hospital" label="就诊医院" align="center" />
        <el-table-column prop="office" label="就诊科室" align="center" />
		<el-table-column label="操作" align="center" width="200">
			<template slot-scope='scope'>
				<p v-if="scope.row.affirm==0">
					<el-button size="mini" type="danger" @click='affirm(scope.row)'>确认</el-button>
				</p>
				<p v-if="scope.row.affirm==1">
					<el-button size="mini" type="success" disabled>已上链</el-button>
				</p>
			</template>
		</el-table-column>
      </el-table>
	  
	  <!-- 输入私钥 第一层弹出栏 -->
	  <el-col :span="12">
		  <el-dialog title="输入私钥" :visible.sync="open" width="50%">
			  <el-form ref="privatekey" :model="privatekey" label-width="100px" :inline="true">
				  <el-input v-model="privatekey.key" type="text" auto-complete="off" placeholder="请输入私钥"></el-input>
			  </el-form>
			  <el-button type="primary" @click="inputkey('privatekey')" style="margin-top: 20px;">确定</el-button>
		  </el-dialog>
	  </el-col>
	  
	  <!-- 查看 第二层弹出栏 -->
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
			  <el-form-item label="档案创建时间:" style="margin-left: 30px;">
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
	<div v-if="loading" class="loading-overlay">
	    <div class="loading-spinner"></div>
		<div style="color: white;">&nbsp;&nbsp;病历内容正在上链</div>
	</div>
  </div>
</template>

<script>

export default {
  name: 'Table',
  data() {
    return {
	  //注入patient对象
	  patient:'',
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
	    age: '',
	    description: '',
	    remark: ''
	  },
      //弹出框显示/隐藏
	  open:false,
      secondopen: false,
	  // 加载提示
	  loading: false, 
    }
  },
  created() {
  	const _this = this
	let patient = JSON.parse(window.localStorage.getItem('patient'))
	_this.patient = patient
	axios.get('http://localhost:8181/record/patientlist/1/'+ _this.pageSize + '/' + _this.patient.id).then(function(resp){
		console.log(resp)
		_this.tableData = resp.data.data.data
		_this.total = resp.data.data.total
	})
  },
  methods: {
    
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
				  //console.log(_this.decryptForm)
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
	affirm(row){
		const _this = this
		_this.loading = true;
		axios.put('http://localhost:8181/record/affirmupdate/'+ row.id).then(function(resp){
			_this.loading = false;
			if(resp.data.code == 0){
				alert('已确认档案！')
				window.location.reload()
			}else{
				alert('确认失败，请稍后重试！')
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
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>