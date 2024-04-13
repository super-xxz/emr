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
		<el-table-column prop="createtime" label="创建时间" align="center" sortable />
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
				<p v-if="scope.row.haveauthority==1">
					<el-button size="mini" disabled type="success">已授权</el-button>
				</p>
				<p v-else>
					<el-button size="mini" @click='apply(scope.row)' type="danger">申请授权</el-button>
				</p>
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
	  //申请授权参数对象
	  applyForm:{
		recordid:'',
		doctorid:'',
	  },
	  //当前页数
	  currentPage: 1,
	  //每页数据条数
	  pageSize: 10,
      // 数据总条数
      total: 0,
      // 表格数据数组
      tableData: [],
    }
  },
  created() {
  	const _this = this
	let doctor = JSON.parse(window.localStorage.getItem('doctor'))
	_this.doctor = doctor
	_this.listQuery.page = 1
	_this.listQuery.doctorid = _this.doctor.id
	axios.get('http://localhost:8181/record/doctorlist',{params:_this.listQuery}).then(function(resp){
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
		axios.get('http://localhost:8181/record/doctorlist',{params:_this.listQuery}).then(function(resp){
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
			  axios.get('http://localhost:8181/record/doctorsearch',{params:_this.listQuery}).then(function(resp){
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
	//申请授权
	apply(row){
		console.log(row)
		const _this = this
		let doctor = JSON.parse(window.localStorage.getItem('doctor'))
		_this.doctor = doctor
		_this.applyForm.doctorid = _this.doctor.id
		_this.applyForm.recordid = row.id
		console.log(_this.applyForm)
		axios.get('http://localhost:8181/authorize/apply',{params:_this.applyForm}).then(function(resp){
			if(resp.data.code == 0){
				alert('已申请，等待用户授权')
			}else{
				alert('申请失败，请稍后重试')
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