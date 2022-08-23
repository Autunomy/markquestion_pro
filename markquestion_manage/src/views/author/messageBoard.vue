<template>
<div>
  <!--搜索框-->
  <div style="width: 100%;padding-top: 20px;">
    <el-form style="width: 100%" :inline="true" class="demo-form-inline">
      <el-form-item style="width: 400px;">
        <el-input style="width: 400px;margin-left:20px;" v-model="searchCondition" placeholder="根据留言信息搜索"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 20px" type="primary" @click="searchMessage">搜索</el-button>
      </el-form-item>
      <el-button style="margin-left: 20px" type="info" @click="cleanSearch">清空搜索条件</el-button>
    </el-form>
  </div>

  <el-table
    :data="messageBoard"
    border
    style="width: 100%;">
    <el-table-column
      fixed
      prop="id"
      label="编号"
      width="80px">
    </el-table-column>
    <el-table-column
      prop="message"
      label="留言信息">
      <template v-slot="scope">
        <div v-highlight>
          <div v-html="scope.row.message"></div>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="createDate"
      label="留言时间"
      width="200px">
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="100">
      <template slot-scope="scope">
        <el-button @click="deleteMessage(scope.row.id)" type="text" size="small" style="color: red">删除</el-button>
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
    style="text-align: center;margin-top: 20px;margin-bottom: 50px">
  </el-pagination>
</div>
</template>

<script>
import qs from "qs";
import authorApi from "@/api/author";
import dateFormat from "@/utils/dateFormat";
import {marked} from "marked";
import questionApi from "@/api/question";

export default {
  name: "messageBoard",
  data(){
    return{
      messageBoard:[],
      //分页信息
      pageInfo:{
        currentPage:1,
        pageSize:5,
        total:0
      },
      //搜索条件
      searchCondition:""
    }
  },
  mounted() {
    this.getMessageBoard();
  },
  methods:{
    getMessageBoard(){
      authorApi.getMessageBoard(qs.stringify(this.pageInfo)).then(resp => {
        this.messageBoard = resp.data
        this.messageBoard.forEach(message => {
          message.createDate = dateFormat.dateFormat(new Date(message.createDate))
          message.message = marked(message.message);
        })
        this.pageInfo = resp.pageInfo
      })
    },
    deleteMessage(id){
      //消息弹出框
      this.$confirm('是否要删除当前留言(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        authorApi.deleteMessage(id).then(resp => {
          if (resp.code === 200) {
            this.$message.success({
              message: "删除成功",
              duration: 1000
            })
          } else {
            this.$message.error({
              message: "删除失败",
              duration: 1000
            })
          }
          if (this.messageBoard.length === 1 && this.pageInfo.currentPage > 1) {
            this.pageInfo.currentPage--;
          }
          this.getMessageBoard();
        }).catch(error => {
          this.$message.error({
            message: "网络出错啦"+error,
            duration: 1000
          })
        })
      }).catch(() => {
        //点击取消按钮
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //当前页码改变的时候的回调函数
    handleCurrentPage(currentPage) {
      //更新当前页码
      this.pageInfo.currentPage = currentPage
      //判断当前显示的是搜索信息还是全部信息
      if(this.searchCondition !== "" && this.searchCondition !== null){
        this.searchMessage()
      }else{
        //访问服务器数据
        this.getMessageBoard()
      }
    },
    //清空搜索条件
    cleanSearch(){
      this.searchCondition = ""
      this.getMessageBoard();
    },
    //搜索
    searchMessage(){
      //搜索条件为空表示搜索全部信息
      if(this.searchCondition !== "" && this.searchCondition !== null){
        if((this.pageInfo.total / this.pageInfo.pageSize) + 1 < this.pageInfo.currentPage){
          this.pageInfo.currentPage = 1
        }
        console.log((this.pageInfo.total / this.pageInfo.pageSize) + 1 < this.pageInfo.currentPage)
        //搜索条件
        let data = "search=" + this.searchCondition + "&" + qs.stringify(this.pageInfo);
        authorApi.searchMessage(data).then(resp => {
          if(resp.code === 200){
            this.messageBoard = resp.data;
            this.messageBoard.forEach(message => {
              message.createDate = dateFormat.dateFormat(new Date(message.createDate))
              message.message = marked(message.message);
            })
            this.pageInfo = resp.pageInfo;
          }
        })
      }else{
        this.pageInfo.currentPage = 1;
        this.getMessageBoard();
      }
    }

  }
}
</script>

<style scoped>

</style>
