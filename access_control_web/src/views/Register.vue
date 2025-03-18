<template>
  <el-form ref="form" :model="form" label-width="80px" >
    <el-row>
      <el-col :span="6" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="姓名">
            <el-input v-model="form.name" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-form-item label="性别">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
        </el-form-item>
      </el-col>
      <el-col :span="6"></el-col>
    </el-row>
    <el-row>
      <el-col :span="6" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="学号">
            <el-input v-model="form.studentId" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-form-item label="学院">
            <el-input v-model="form.college" ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="6" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="专业">
            <el-input v-model="form.major" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-form-item label="手机号">
            <el-input v-model="form.phone" ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="人脸照片">
            <el-upload
                class="avatar-uploader borderGrey"
                :action="this.uploadUrl"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
              <img v-if="this.imageUrl" :src="this.imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item>
          <el-button type="primary" @click="onSubmit">确定</el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "register",
  data() {
    return {
      form: {
        sex:'男'
      },
      imageUrl:'',
      uploadUrl: this.$api.http+"/user-info/upload"
    }
  },
  methods: {
    onSubmit() {
      console.log("添加数据",this.form)
      console.log(this.form.faceUrl?true:false)
      if (!this.form.faceUrl){
        this.$message.error("请上传人脸照片")
        return
      }
      this.$axios.post(this.$api.http+'/user-info/add',this.form).then(res=>{
        if (res.data.code == 0){
          this.$message.success("注册成功")
        }else {
          this.$message.error(res.data.msg)
        }
      })
    },
    handleAvatarSuccess(res) {
      if (res.code == 0){
        this.$message.success("上传成功")
        this.form.faceUrl = res.data
        this.imageUrl = this.$api.http+res.data
      }else {
        this.$message.error(res.msg)
      }
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>

<style scoped>
  .showBoder{
    border: 1px solid red;
  }
  .borderGrey{
    border: 1px solid #2c3e50;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>