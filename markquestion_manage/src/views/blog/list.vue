<template>
  <div style="margin-bottom: 20px;">
    <!--搜索框-->
    <div style="width: 100%;padding-top: 20px;">
      <el-form style="width: 100%" :inline="true" class="demo-form-inline">
        <el-form-item style="width: 400px;">
          <el-input style="width: 400px;margin-left:20px;" v-model="searchCondition" placeholder="根据博客名称搜索"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="margin-left: 20px" type="primary" @click="searchBlog">搜索</el-button>
        </el-form-item>
        <el-button style="margin-left: 20px" type="info" @click="cleanSearch">清空搜索条件</el-button>
      </el-form>
    </div>

    <el-table
      :data="blogList"
      border
      style="width: 100%;">
      <el-table-column
        fixed
        prop="id"
        label="编号"
        width="80px">
      </el-table-column>
      <el-table-column
        prop="title"
        label="博客标题">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="className"
        label="分类专栏">
      </el-table-column>
      <el-table-column
        prop="watch"
        label="浏览量">
      </el-table-column>
      <el-table-column
        prop="descp"
        label="描述"
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
          <el-button type="text" size="small" @click="updateBlog(scope.row)">编辑</el-button>
          <el-button @click="deleteBlog(scope.row)" type="text" size="small" style="color: red">删除</el-button>
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
        <el-table-column min-width="100px" prop="createTime" label="评论日期"></el-table-column>
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
import blogApi from "@/api/blog";
import dateFormat from "@/utils/dateFormat";
import qs from "qs";

export default {
  name: "list",
  data(){
    return{
      searchCondition:"",
      blogList:[],
      pageInfo:{
        currentPage:1,
        pageSize:10,
        total:0
      },
      isShowComment:false,
      commentList:[],
      commentPageInfo:{
        currentPage:1,
        pageSize:10,
        total:0
      }
    }
  },
  mounted() {
    this.queryAllBlog();
  },
  methods:{
    //获取所有的博客
    queryAllBlog(){
      blogApi.queryAllBlog(qs.stringify(this.pageInfo)).then(resp => {
        if(resp.code === 200){
          this.blogList = resp.data;
          this.pageInfo = resp.pageInfo;
          //格式化日期时间
          this.blogList.forEach(blog => {
            blog.createTime = dateFormat.dateFormat(new Date(blog.createTime));
          })
        }else{
          this.$message.warning("获取博客列表失败")
        }
      }).catch(error => {
        this.$message.error("获取失败-"+error)
      })
    },
    //根据标题搜索博客
    searchBlog(){},
    //删除搜索条件
    cleanSearch(){},
    //展示某个博客的评论
    showComment(){},
    //修改博客
    updateBlog(){},
    //删除博客
    deleteBlog(){},
    //博客分页信息发生变化
    handleCurrentPage(){},
    //评论信息发生变化
    commentCurrentPage(){},
    //删除一个评论
    deleteComment(){},
  }
}
</script>

<style scoped>

</style>
