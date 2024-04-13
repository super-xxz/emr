<template>
	<div>
		<!-- 表格栏 -->
		<el-table
		  :data="tableData"
		  tooltip-effect="dark"
		  style="width: 100%"
		  size="medium"
		>
		  <el-table-column prop="createtime" label="申请时间" align="center"sortable />
		  <el-table-column prop="name" label="医生姓名" align="center"/>
		  <el-table-column prop="hospital" label="医生所属机构" align="center" />
		  <el-table-column prop="office" label="医生所属科室" align="center" />
		  <el-table-column prop="recordid" label="申请病历编号" align="center"/>
		  <el-table-column prop="state" label="状态" align="center">
		  	<template slot-scope="scope">
		  		<p v-if="scope.row.state==0">未授权</p>
		  		<p v-if="scope.row.state==1">已授权</p>
		  	</template>
		  </el-table-column>
		  <el-table-column label="操作" align="center" width="200">
					<template slot-scope='scope'>
						<p v-if="scope.row.state==0">
						<el-button size="mini"@click='apply(scope.row)'>授权</el-button>
						</p>
						<p v-if="scope.row.state==1">
						<el-button size="mini"@click='cancelapply(scope.row)'>取消授权</el-button>
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
		
		<!-- 输入私钥 弹出栏 -->
		<el-col :span="12">
			  <el-dialog title="输入私钥" :visible.sync="open" width="50%">
				  <el-form ref="applyForm" :model="applyForm" label-width="100px" :inline="true">
					  <el-input v-model="applyForm.privateKey" type="text" auto-complete="off" placeholder="请输入私钥"></el-input>
				  </el-form>
				  <el-button type="primary" @click="inputkey('applyForm')" style="margin-top: 20px;">确定</el-button>
			  </el-dialog>
			  <div v-if="loading" class="loading-overlay">
			      <div class="loading-spinner"></div>
				  <div style="color: white;">&nbsp;&nbsp;正在从区块链上获取病历</div>
			  </div>
		</el-col>
	</div>
</template>

<script>
	export default{
		data(){
			return{
				// 表格数据数组
				tableData: [],
				//当前页数
				currentPage: 1,
				//每页数据条数
				pageSize: 10,
				// 数据总条数
				total: 0,
				//向后端传私钥和授权id
				applyForm:{
					privateKey:'',
					roleId:0,
				},
				//弹出框显示/隐藏
				open:false,
				// 加载提示
				loading: false, 
			}
		},
		created() {
		  const _this = this
		  let patient = JSON.parse(window.localStorage.getItem('patient'))
		  _this.patient = patient
		  axios.get('http://localhost:8181/authorize/list/1/'+_this.pageSize+ '/' + _this.patient.id).then(function(resp){
		  	console.log(resp)
		  	_this.tableData = resp.data.data.data
		  	_this.total = resp.data.data.total
		  })
		},
		methods: {
			apply(row){
				console.log(row)
				const _this = this
				_this.open = true;
				_this.applyForm.rowId = row.id
			},
			
			inputkey(formName){
				this.open = false
				this.$refs["applyForm"].validate((valid) => {
					  if(valid){
						  const _this = this
						  _this.loading = true;
						  console.log("privateKey:"+_this.applyForm.privateKey)
						  console.log("rowId:"+_this.applyForm.rowId)
						  axios.put('http://localhost:8181/authorize/applyupdate',_this.applyForm).then(function(resp){
							_this.loading = false;
						  	if(resp.data.code == 0){
						  		alert('已同意授权！')
						  		window.location.reload()
						  	}else{
						  		alert('授权失败，请检查私钥是否正确并稍后重试！')
						  	}
						  })
						  // 重置输入框的值
						  _this.privatekey.key = '';
					  }else{
						  alert('请输入私钥!');
						  return false;
					  }
				})
			},
			
			cancelapply(row){
				const _this = this
				axios.put('http://localhost:8181/authorize/cancelupdate/'+ row.id).then(function(resp){
					if(resp.data.code == 0){
						alert('已取消授权！')
						window.location.reload()
					}else{
						alert('取消授权失败！')
					}
				})
			},
		}
	}
</script>

<style>
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