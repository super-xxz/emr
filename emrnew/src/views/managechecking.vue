<!--资质码管理-->
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
		<el-form-item label="医院" prop="hospital" style="margin-left: 160px;">
          <el-input v-model="listQuery.hospital" placeholder="医院" />
        </el-form-item>
		<el-form-item label="科室" prop="office">
          <el-input v-model="listQuery.office" placeholder="医院" />
        </el-form-item>
        <el-form-item>
			<el-button @click="resetForm('listQuery')" style="margin-left: 30px;">重置</el-button>
        </el-form-item>
		<el-form-item>
			<el-button type="primary" @click="onSubmit('listQuery')" style="margin-left: 10px;">查询</el-button>
		</el-form-item>
      </el-form>
	  
      <!-- 表格栏 -->
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        size="medium"
		:loading="loading"
      >
        <el-table-column prop="hospital" label="医院" align="center" />
        <el-table-column prop="office" label="科室" align="center" />
		<el-table-column prop="checking" label="资质码" align="center" />
        <el-table-column label="操作" align="center" width="200">
			<template slot-scope='scope'>
				<el-button size="mini" @click='resetChecking(scope.row)'>重置</el-button>
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
      // 查询列表参数对象
      listQuery: {
		hospital:'',
        office: '',
        page: '',
        size: 10
      },
	  //当前页数
	  currentPage: 1,
	  //每页数据条数
	  pageSize: 10,
      // 数据总条数
      total: 0,
      // 表格数据数组，存储查询数据结果
      tableData: [],
	  // 加载提示
	  loading: true, 
    }
  },
  created() {//生命周期钩子，初始化为1页
  	const _this = this
	_this.listQuery.page = 1
	axios.get('http://localhost:8181/checking/checkingsearch',{params:_this.listQuery}).then(function(resp){
		console.log(resp)
		_this.tableData = resp.data.data.data//请求到的数据用来填充这俩
		_this.total = resp.data.data.total 
	})
  },
  methods: {
	//页面切换，更新当前页数并重新发起请求
	page(currentPage){
		const _this = this
		_this.listQuery.page = _this.currentPage
		axios.get('http://localhost:8181/checking/checkingsearch',{params:_this.listQuery}).then(function(resp){
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
			  _this.listQuery.page = _this.currentPage
			  axios.get('http://localhost:8181/checking/checkingsearch',{params:_this.listQuery}).then(function(resp){
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
	//重置资质码
	resetChecking(row) {
		console.log(row)
		const requestData = {
			hospital: row.hospital,
			office: row.office
		};
	    axios.post('http://localhost:8181/checking/resetchecking', requestData)
		.then(response => {
			// 处理成功的情况
			if (response.data.code === 0) {
				// 弹出提示窗口
				this.$alert('资质码重置成功', '', {
					confirmButtonText: '确定',
					showClose: false, // 隐藏右上角叉号按钮
					callback: action => {
						// 在用户点击确定按钮后刷新页面
						if (action === 'confirm') {
							window.location.reload();
						}
					}
				});
			} else {
				// 处理失败的情况
				this.$message.error('资质码重置失败，请稍后再试');
			}
		})
		.catch(error => {
			console.error('重置资质码请求出错:', error);
			this.$message.error('请求出错，请稍后再试');
		});//当POST请求发生错误时，捕获异常并打印错误信息到控制台
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