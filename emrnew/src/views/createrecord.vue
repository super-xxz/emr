<template>
	<div style="margin-left: 300px;">
		<div class="record">
			<el-col :span="12">
				<!-- 表单 -->
				<el-form :model="createForm" :rules="rules" ref="createForm" label-position="labelPosition" label-width="100px" class="demo-createForm">
					<el-form-item label="姓名" prop="name">
						<label slot="label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</label>
						<el-input placeholder="请输入姓名" v-model="createForm.name"></el-input>
					</el-form-item>
					<el-form-item label="联系电话" prop="phone">
						<el-input placeholder="请输入联系电话" v-model="createForm.phone"></el-input>
					</el-form-item>
					<el-form-item label="医生诊断" prop="description">
						<el-input type="textarea" placeholder="请输入诊断内容" v-model="createForm.description"></el-input>
					</el-form-item>
					<el-form-item label="备注" prop="remark">
						<label slot="label">&nbsp;&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</label>
						<el-input type="textarea" placeholder="备注" v-model="createForm.remark"></el-input>
					</el-form-item>
				</el-form>

				<!-- 按钮 -->
				<div class="subbutton">
					<el-button type="primary" @click="create('createForm')">+ 新建档案</el-button>
					<el-button @click="resetForm('createForm')" style="margin-left: 40px">重置</el-button>
				</div>
			</el-col>
		</div>
		
		<!-- 弹窗 -->
		<el-dialog
		  title="确认档案"
		  :visible.sync="dialogVisible"
		  width="50%">
			<span>
				<el-form :model="createForm" :inline="true" label-width="100px" class="demo-createForm">
					<el-form-item label="就诊医院:">
						{{doctor.hospital}}
					</el-form-item>
					<el-form-item label="就诊科室:">
						{{doctor.office}}
					</el-form-item>
					<el-form-item label="主治医师:">
						{{doctor.name}}
					</el-form-item>
					<br />
					<el-form-item label="姓名:">
						<label slot="label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
						{{createForm.name}}
					</el-form-item>
					<el-form-item label="联系电话:">
						{{createForm.phone}}
					</el-form-item>
					<br /><hr />
					<el-form-item label="医生诊断:">
						{{createForm.description}}
					</el-form-item>
					<br />
					<el-form-item label="备注">
						<label slot="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
						{{createForm.remark}}
					</el-form-item>
				</el-form>
			</span>
		  <span slot="footer" class="dialog-footer">
		    <el-button @click="dialogVisible=false">取 消</el-button>
		    <el-button type="primary" @click="submitForm('createForm')">确 定</el-button>
		  </span>
		</el-dialog>
	</div>
</template>

<script>

	export default{
		data(){
			return{
				doctor:'',
				labelPosition:'left',
				dialogVisible: false,
				createForm:{
					doctorid:'',
					name:'',
					phone:'',
					hospital:'',
					office:'',
					description:'',
					remark:'',
				},
				rules:{
					name:[
						{ required: true, message: '请输入姓名', trigger: 'blur' },
					],
					phone:[
						{ required: true, message: '请输入联系电话', trigger: 'blur' },
					],
					description:[
						{ required: true, message: '请输入诊断内容', trigger: 'blur' },
					],
				},
			};
		},
		created() {
		  let doctor = JSON.parse(window.localStorage.getItem('doctor'))
				this.doctor = doctor
		},
		methods: {
			//点击新建档案
		    create(formName) {
				const _this=this
		        this.$refs[formName].validate((valid) => {
		          if (valid) {
		            //打开弹窗
					this.dialogVisible=true
		          } else {
		            alert('请完善内容!');
		            return false;
		          }
		        });
		    },
			
			//确认档案
			submitForm(formName) {
				const _this = this
				_this.createForm.doctorid = _this.doctor.id
				_this.createForm.hospital = _this.doctor.hospital
				_this.createForm.office = _this.doctor.office
				console.log(_this.createForm)
				axios.post('http://localhost:8181/record/create',_this.createForm).then(function (resp){
					if(resp.data.code==0){
						_this.$alert('新建成功!','',{
							confirmButtonText:'确定',
						})
						_this.$refs[formName].resetFields()
						_this.dialogVisible=false
					}else{
						alert('新建失败，请检查姓名和电话是否有误！')
						_this.dialogVisible=false
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