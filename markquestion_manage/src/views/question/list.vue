<template>
<div style="margin-bottom: 20px">
  <!--搜索框-->
  <div style="width: 100%;padding-top: 20px">
    <el-form style="width: 100%" :inline="true" class="demo-form-inline">
      <el-form-item style="width: 400px;">
        <el-input style="width: 400px;margin-left:20px;" v-model="search" placeholder="根据编号,题目名称搜索"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 20px" type="primary" @click="searchQuestion">搜索</el-button>
      </el-form-item>
      <el-form-item label="题目来源">
        <el-select placeholder="题目来源">
          <el-option label="力扣(leetcode)" value="力扣(leetcode)"></el-option>
          <el-option label="ACWing" value="ACWing"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="难度">
        <el-select placeholder="难度">
          <el-option label="简单" value="简单"></el-option>
          <el-option label="中等" value="中等"></el-option>
          <el-option label="困难" value="困难"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>

  <el-table
    :data="questionList"
    border
    style="width: 100%">
    <el-table-column
      fixed
      prop="id"
      label="编号">
    </el-table-column>
    <el-table-column
      prop="questionName"
      label="题目名称">
    </el-table-column>
    <el-table-column
      prop="questionFrom"
      label="题目来源">
    </el-table-column>
    <el-table-column
      prop="author"
      label="作者">
    </el-table-column>
    <el-table-column
      prop="tag"
      label="语言">
      <template v-slot="scope">
        <el-tag
          type="primary">
          {{scope.row.tag}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column
      prop="level"
      label="难度">
      <template v-slot="scope">
        <el-tag
          :type="scope.row.level === '简单'? 'success' : scope.row.level === '中等'? 'warning' : 'danger'"
          effect="dark"
        >
          {{scope.row.level}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column
      prop="watch"
      label="浏览量">
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="150">
      <template slot-scope="scope">
        <el-button type="text" size="small" @click="updateQuestion(scope.row)">编辑</el-button>
        <el-button @click="deleteQuestion(scope.row)" type="text" size="small" style="color: red">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!--分页-->
  <el-pagination
    background
    layout="prev, pager, next"
    :current-page="pageInfo.currentPage"
    :page-size="pageInfo.pageSize"
    :total="pageInfo.total"
    @current-change="handleCurrentPage"
    style="text-align: center;margin-top: 20px">
  </el-pagination>
</div>
</template>
<script>
import questionApi from "@/api/question";
import qs from "qs";
export default {
  name: "list",
  data(){
    return{
      questionList:[],
      pageInfo:{
        currentPage:1,
        pageSize:10,
        total:0
      },
      //查询条件
      search:""
    }
  },
  mounted() {
    this.queryQuestionPage();
  },
  methods:{
    //分页查询题解
    queryQuestionPage(){
      questionApi.queryQuestionPage(qs.stringify(this.pageInfo)).then(resp => {
        if(resp.code === 200){
          this.questionList = resp.data
          this.pageInfo = resp.pageInfo
        }
      })
    },
    //删除题解
    deleteQuestion(row){
      console.log(row)
    },
    //当前页码改变的时候的回调函数
    handleCurrentPage(currentPage) {
      //更新当前页码
      this.pageInfo.currentPage = currentPage
      //访问服务器数据
      this.queryQuestionPage()
    },
    searchQuestion(){

    },
    //修改题解 跳转到题解修改页面
    updateQuestion(data){
      this.$router.push("/question/updateQuestion?id="+data.id)
    }
  }
}
</script>

<style scoped>

</style>
