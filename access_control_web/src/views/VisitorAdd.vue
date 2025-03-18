<template>
  <el-form ref="form" :model="form" label-width="80px" >
    <el-row>
      <el-col :span="6" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="姓名" label-width="100px">
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
        <el-form-item label="身份证号" label-width="100px">
          <el-input v-model="form.idCard" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-form-item label="受访人姓名" label-width="100px">
          <el-input v-model="form.visitorName" ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="6" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="受访人学院" label-width="100px">
          <el-input v-model="form.visitorCollege" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-form-item label="手机号" label-width="100px">
          <el-input v-model="form.phone" ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="6" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="进入时间">
          <el-date-picker
              v-model="form.visitorTime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetime"
              placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="6" style="border: 1px solid white"></el-col>
      <el-col :span="4">
        <el-form-item label="出去时间">
          <el-date-picker
              v-model="form.outTime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetime"
              placeholder="选择日期时间">
          </el-date-picker>
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
  name: "VisitorAdd",
  data() {
    return {
      form: {
        sex:'男'
      },
    }
  },
  methods: {
    onSubmit() {
      this.$axios.post(this.$api.http+'/visitor/add',this.form).then(res=>{
        if (res.data.code == 0){
          this.$message.success("登记成功")
        }else {
          this.$message.error(res.data.msg)
        }
      })
    },

  }
}
</script>

<style scoped>

</style>