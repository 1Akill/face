<template>
  <el-container style="border: 1px solid #eee">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu :default-openeds="['3']" router>
        <el-submenu index="3">
          <template slot="title"><i class="el-icon-setting"></i>系统</template>
          <el-menu-item-group>
            <el-menu-item index="/register">用户注册</el-menu-item>
            <el-menu-item index="/userList">用户管理</el-menu-item>
            <el-menu-item index="/accessRecord">出入记录</el-menu-item>
            <el-menu-item index="/visitorAdd">访客登记</el-menu-item>
            <el-menu-item index="/visitorList">访客记录</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px">
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="onExit()">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span>{{this.userInfo.userName}}</span>
      </el-header>
      <el-main>
          <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "MainMenu",
  data() {
    return {
      userInfo:{}
    }
  },
  created() {
    const userInfoString = localStorage.getItem("userInfo");
    if (userInfoString) {
      this.userInfo = JSON.parse(userInfoString);
    }
    if (this.$route.path === "/main") {
      // Redirect to the default child route (e.g., /register)
      this.$router.push("/userList");
    }
  },
  methods:{
    onExit(){
      console.log("退出")
      localStorage.removeItem("userInfo");
      localStorage.removeItem("Auth-token");
      this.$router.push("/");
    }
  }
}
</script>

<style scoped>

</style>