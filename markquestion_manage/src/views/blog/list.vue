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
export default {
  name: "list",
  data(){
    return{

    }
  }
}
</script>

<style scoped>

</style>
