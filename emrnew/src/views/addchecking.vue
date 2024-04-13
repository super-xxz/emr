<template>
	<div style="margin-left: 300px;">
		<div class="checking">
			<el-col :span="12">
				<!-- 表单 -->
				<el-form :model="createForm" :rules="rules" ref="createForm" label-position="labelPosition" label-width="100px" class="demo-createForm">
					<el-form-item label="医院" prop="hospital">
						<label slot="label">医&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;院</label>
						<el-input placeholder="请输入医院" v-model="createForm.hospital"></el-input>
					</el-form-item>
					<el-form-item label="科室" prop="office">
						<label slot="label">科&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;室</label>
						<el-input placeholder="请输入科室" v-model="createForm.office"></el-input>
					</el-form-item>
				</el-form>

				<!-- 按钮 -->
				<div class="subbutton">
					<el-button type="primary" @click="create('createForm')">+ 生成资质码</el-button>
					<el-button @click="resetForm('createForm')" style="margin-left: 40px">重置</el-button>
				</div>
			</el-col>
		</div>
	</div>
</template>

<script>

	export default{
		data(){
			return{
				labelPosition:'left',
				createForm:{
					hospital:'',
					office:'',
				},
				rules:{
					hospital:[
						{ required: true, message: '请输入医院', trigger: 'blur' },
					],
					office:[
						{ required: true, message: '请输入科室', trigger: 'blur' },
					],
				},
			};
		},
		created() {
		  
		},
		methods: {
			//新建资质
			create(formName) {
				const _this = this
				console.log(_this.createForm)
				axios.post('http://localhost:8181/checking/create',_this.createForm).then(function (resp){
					if(resp.data.code==0){
						_this.$alert('新建成功! 校验码为：'+ resp.data.data,'',{
							confirmButtonText:'确定',
						})
						_this.$refs[formName].resetFields()
					}
					if(resp.data.code==-1){
						_this.$alert('数据库内已有该医院及科室!','',{
							confirmButtonText:'确定',
						})
						_this.$refs[formName].resetFields()
					}
					if(resp.data.code==-2){
						_this.$alert('新建失败，请稍后再试!','',{
							confirmButtonText:'确定',
						})
						_this.$refs[formName].resetFields()
					}
				})
			},
			
			//重置
			resetForm(formName) {
			    this.$refs[formName].resetFields();
			}
		
		}
	}
</script>

<style>
	.subbutton{
		margin-left: 110px;
	}
	


</style>