<template>
<div style="margin-bottom: 20px;">
  <!--搜索框-->
  <div style="width: 100%;padding-top: 20px;">
    <el-form style="width: 100%" :inline="true" class="demo-form-inline">
      <el-form-item style="width: 400px;">
        <el-input style="width: 400px;margin-left:20px;" v-model="searchCondition" placeholder="根据编号,题目名称,题目来源,作者搜索"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 20px" type="primary" @click="searchQuestion">搜索</el-button>
      </el-form-item>
<!--      <el-form-item label="题目来源">-->
<!--        <el-select v-model="searchCondition.platform" placeholder="题目来源">-->
<!--          <el-option label="力扣(leetcode)" value="力扣(leetcode)"></el-option>-->
<!--          <el-option label="ACWing" value="ACWing"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="难度">-->
<!--        <el-select v-model="searchCondition.level" placeholder="难度">-->
<!--          <el-option label="简单" value="简单"></el-option>-->
<!--          <el-option label="中等" value="中等"></el-option>-->
<!--          <el-option label="困难" value="困难"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-button style="margin-left: 20px" type="info" @click="cleanSearch">清空搜索条件</el-button>
    </el-form>
  </div>

  <el-table
    :data="questionList"
    border
    style="width: 100%;">
    <el-table-column
      fixed
      prop="id"
      label="编号"
      width="80px">
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
      prop="createTime"
      label="发布日期">
    </el-table-column>
    <el-table-column
      prop="updateTime"
      label="最后一次修改日期">
    </el-table-column>
    <el-table-column
      prop="tag"
      label="语言"
      width="80px">
      <template v-slot="scope">
        <el-tag
          type="primary">
          {{scope.row.tag}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column
      prop="level"
      label="难度" width="80px">
      <template v-slot="scope">
        <el-tag
          :type="scope.row.level === '简单'? 'success' : scope.row.level === '中等'? 'warning' : 'danger'"
          effect="dark">
          {{scope.row.level}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column
      prop="watch"
      label="浏览量"
      width="80px">
    </el-table-column>
    <el-table-column
      label="评论"
      width="80px">
      <template slot-scope="scope">
        <el-button type="text" size="small" @click="showComment(scope.row.id)">查看评论</el-button>
      </template>
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

  <!--评论展示的弹出框-->
  <el-dialog title="评论" :visible.sync="isShowComment">
    <el-table :data="commentList">
      <el-table-column prop="id" label="id"></el-table-column>
      <el-table-column prop="content" label="内容"></el-table-column>
      <el-table-column min-width="100px" prop="commentDate" label="评论日期"></el-table-column>
      <el-table-column prop="userId" label="用户id"></el-table-column>
      <el-table-column prop="isReply" label="是否是回复评论"></el-table-column>
      <el-table-column prop="commentId" label="回复的评论的id"></el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button @click="deleteComment(scope.row)" type="text" size="small" style="color: red">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :current-page="commentPageInfo.currentPage"
      :page-size="commentPageInfo.pageSize"
      :total="commentPageInfo.total"
      @current-change="commentCurrentPage"
      style="text-align: center;margin-top: 20px">
    </el-pagination>
  </el-dialog>
</div>
</template>
<script>
import questionApi from "@/api/question";
import commentApi from "@/api/comment";
import dateFormat from "@/utils/dateFormat";
import qs from "qs";
import {marked} from "marked"

export default {
  name: "list",
  data(){
    return{
      questionList:[],
      //题解的分页信息
      pageInfo:{
        currentPage:1,
        pageSize:10,
        total:0
      },
      //评论的分页信息
      commentPageInfo:{
        currentPage:1,
        pageSize:5,
        total:0
      },
      //题解的评论
      commentList:[],
      //展示评论弹出框
      isShowComment:false,
      //查询条件
      searchCondition:""
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
          this.questionList.forEach(question => {
            question.createTime = dateFormat.dateFormat(new Date(question.createTime))
            question.updateTime = dateFormat.dateFormat(new Date(question.updateTime))
          })
          this.pageInfo = resp.pageInfo
        }
      })
    },
    //删除题解
    deleteQuestion(data){
      //消息弹出框
      this.$confirm('是否要删除当前题解(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //点击了确定按钮 那么就删除当前院系
        questionApi.deleteQuestionById(data.id).then(resp => {
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
          if (this.questionList.length === 1 && this.pageInfo.currentPage > 1) {
            this.pageInfo.currentPage--;
          }
          this.queryQuestionPage();
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
      //访问服务器数据
      this.queryQuestionPage()
    },
    //修改题解 跳转到题解修改页面
    updateQuestion(data){
      this.$router.push("/question/updateQuestion?id="+data.id)
    },
    //查询当前题目的所有评论
    showComment(id){
      commentApi.queryAllCommentById("id="+id+"&"+qs.stringify(this.commentPageInfo)).then(resp => {
        this.commentList = resp.data;
        this.commentList.forEach(comment => {
          comment.commentDate = dateFormat.dateFormat(new Date(comment.commentDate))
          if(comment.content.length > 20){
            comment.content = comment.content.substring(0,20)+"...";
          }
        })

        this.commentPageInfo = resp.pageInfo;

        this.isShowComment = true;
      })
    },
    //评论展示框页码发生改变
    commentCurrentPage(currentPage){
      //更新当前页码
      this.commentPageInfo.currentPage = currentPage
      //访问服务器数据
      this.showComment(this.commentList[0].questionId)
    },
    //删除一个评论
    deleteComment(data){
      //消息弹出框
      this.$confirm('是否要删除当前评论(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //点击了确定按钮 那么就删除当前院系
        commentApi.deleteCommentById(data.id).then(resp => {
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
          if (this.commentList.length === 1 && this.commentPageInfo.currentPage > 1) {
            this.commentPageInfo.currentPage--;
          }
          this.showComment(this.commentList[0].questionId);
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
    //清空搜索条件
    cleanSearch(){
      this.searchCondition = "";
      this.queryQuestionPage();
    },
    //搜索题解
    searchQuestion(){
      //搜索条件为空表示搜索全部信息
      if(this.searchCondition !== "" && this.searchCondition !== null){
        //搜索条件
        this.pageInfo.currentPage = 1
        let data = "search=" + this.searchCondition + "&" + qs.stringify(this.pageInfo);
        questionApi.searchQuestion(data).then(resp => {
          if(resp.code === 200){
            this.questionList = resp.data;
            this.questionList.forEach(question => {
              question.createTime = dateFormat.dateFormat(new Date(question.createTime))
              question.updateTime = dateFormat.dateFormat(new Date(question.updateTime))
            })
            this.pageInfo = resp.pageInfo;
          }
        })
      }else{
        this.pageInfo.currentPage = 1;
        this.queryQuestionPage();
      }
    }
  }
}
</script>

<style scoped>

</style>
