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
      <el-table-column prop="recordId" label="编号" width="140">
      </el-table-column>
      <el-table-column prop="studentId" label="学号" width="120">
      </el-table-column>
      <el-table-column prop="sex" label="性别">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="140">
      </el-table-column>
      <el-table-column prop="college" label="学院" width="120">
      </el-table-column>
      <el-table-column prop="major" label="专业">
      </el-table-column>
      <el-table-column prop="phone" label="手机号">
      </el-table-column>
      <el-table-column prop="joinTime" label="进入时间">
      </el-table-column>
      <el-table-column prop="outTime" label="出去时间">
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
  name: "AccessRecord",
  data(){

    return {
      search:{},
      tableData: [],
      response:{
        current: 1
      },
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
      this.$axios.get(this.$api.http+'/access-record/page',{params:data}).then(res=>{
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
      this.$axios.post(this.$api.http+'/access-record/remove/'+row.recordId).then(res=>{
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