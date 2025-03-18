<template>
  <div id="app" class="background-image">
    <div class="login-container">
      <div class="login-form">
        <h3>管理员登录</h3>
        <el-form :model="userFrom" status-icon ref="ruleForm" label-width="50px" class="demo-ruleForm">
          <el-form-item label="账号" prop="pass">
            <el-input type="text" v-model="userFrom.userName" placeholder="请输入账号" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="checkPass">
            <el-input type="password" v-model="userFrom.password" placeholder="请输入密码" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="login()" class="custom-button">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminLogin",
  data() {
    return {
      userFrom: {
        userName: '',
        password: ''
      }
    };
  },
  methods: {
    login() {
      // eslint-disable-next-line no-debugger
      debugger
      this.$axios.post(this.$api.http+'/admin-info/login',{
        'userName':this.userFrom.userName,
        'password':this.userFrom.password
      }).then(res=>{
        if(res.data.code == 0){
          this.$message({
            message:"登录成功",
            type:'success'
          })
          localStorage.setItem("Auth-token",res.data.data.token)
          console.log(res.data.data)
          localStorage.setItem("userInfo", JSON.stringify(res.data.data))
          this.$router.push('/main')
        }else {
          this.$message.error("账号密码错误")
        }
      });
    },


  }
};
</script>

<style scoped>
body {
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.background-image {
  background-image: url('@/assets/a.png');
  background-size: cover;
  background-position: center;
  min-height: 95vh; /* Set minimum height to viewport height */
  background-attachment: fixed; /* Fixed background attachment */
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-container {
  width: 40%;
  max-width: 400px; /* Set a maximum width for the login container */
}

.login-form {
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
}

.demo-ruleForm {
  width: 100%;
}

.custom-button {
  width: 100%; /* Set button width to 100% */
  margin-top: 20px; /* Add margin to separate from form */
}
</style>
