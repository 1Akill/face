<template>
  <div>
    <el-container>
      <el-form :inline="true" :model="search" class="demo-form-inline">
        <el-form-item label="用户姓名">
          <el-input v-model="search.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">搜素</el-button>
        </el-form-item>
      </el-form>
    </el-container>
    <el-table :data="tableData" >
      <el-table-column prop="id" label="编号" >
      </el-table-column>
      <el-table-column prop="studentId" label="学号">
      </el-table-column>
      <el-table-column prop="name" label="姓名">
      </el-table-column>
      <el-table-column prop="sex" label="性别">
      </el-table-column>
      <el-table-column prop="college" label="学院" width="200">
      </el-table-column>
      <el-table-column prop="major" label="专业" width="200">
      </el-table-column>
      <el-table-column prop="phone" label="手机号">
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="httpUrl+scope.row.faceUrl"
              fit="fill"></el-image>
        </template>
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="100">
        <template slot-scope="scope">
          <el-button @click="handleRemove(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background
        layout="total,prev, pager, next"
        :total="response.total"
        :current-page="response.current"
        @current-change="currentChange"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "userList",
  data(){

    return {
      search:{},
      tableData: [],
      response:{
        current: 1
      },
      httpUrl:this.$api.http
    }
  },
  created() {
    this.getPage()
  },
  methods:{
    onSubmit(){
      this.response.current = 1
      this.getPage()
    },
    getPage(){
      const data = {'current':this.response.current,'size':10,'name':this.search.name}
      this.$axios.get(this.$api.http+'/user-info/page',{params:data}).then(res=>{
        if (res.data.code==0){
          this.tableData = res.data.data.records
          this.response.total = res.data.data.total
          this.response.current = res.data.data.current
        }
      })
    },
    currentChange(val){
      console.log("改变当前页",val)
      this.response.current = val
      this.getPage()

    },
    handleRemove(row){
      this.$axios.post(this.$api.http+'/user-info/remove/'+row.id).then(res=>{
        if (res.data.code==0){
          this.$message({
            message: '删除成功',
            type: 'success'
          });
          this.getPage()
        }
      })
    }
  }
}
</script>

<style scoped>

</style>