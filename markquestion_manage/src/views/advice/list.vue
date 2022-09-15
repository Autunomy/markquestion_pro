<template>
  <div>
    <!--搜索框-->
    <div style="width: 100%;padding-top: 20px;">
      <el-form style="width: 100%" :inline="true" class="demo-form-inline">
        <el-form-item style="width: 400px;">
          <el-input style="width: 400px;margin-left:20px;" v-model="searchCondition"
                    placeholder="根据名称搜索"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="margin-left: 20px" type="primary" @click="searchAdvice">搜索</el-button>
        </el-form-item>
        <el-button style="margin-left: 20px" type="info" @click="cleanSearch">清空搜索条件</el-button>
      </el-form>
    </div>

    <el-table
      :data="adviceList"
      border
      style="width: 100%">
      <el-table-column
        fixed
        prop="id"
        label="id">
      </el-table-column>
      <el-table-column
        prop="adviceName"
        label="推荐名称"
      min-width="100px">
      </el-table-column>
      <el-table-column
        prop="adviceAuthor"
        label="推荐作者"
      min-width="100px">
      </el-table-column>
      <el-table-column
        prop="adivceTag"
        label="标签">
        <template v-slot="scope">
          <el-tag>{{ scope.row.adviceTag }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="adviceWatch"
        label="浏览量">
      </el-table-column>
      <el-table-column
        prop="adviceDescribe"
        label="简介"
        min-width="500px">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="adviceContent"-->
<!--        label="详细描述"-->
<!--        min-width="500px">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="createTime"
        label="创建日期"
        min-width="150px">
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="修改日期"
        min-width="150px">
      </el-table-column>
      <el-table-column
        prop="advice_img"
        label="图片"
        min-width="50px">
        <template v-slot="scope">
          <el-avatar shape="square" :size="'small'" :src="scope.row.adviceImg"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showUpdateAdvice(scope.row)">编辑</el-button>
          <el-button @click="deleteAdvice(scope.row.id)" type="text" size="small" style="color: red">删除</el-button>
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
import adviceApi from "@/api/advice";
import qs from "qs";
import dateFormat from "@/utils/dateFormat";

export default {
  name: "list",
  data() {
    return {
      adviceList: [],
      pageInfo: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      searchCondition:""
    }
  },
  mounted() {
    this.queryAllAdvice();
  },
  methods: {
    queryAllAdvice() {
      adviceApi.queryAllAdvice(qs.stringify(this.pageInfo)).then(resp => {
        console.log(resp)
        this.adviceList = resp.data;
        this.adviceList.forEach(advice => {
          advice.adviceImg = "http://localhost:8001" + advice.adviceImg;
          advice.createTime = dateFormat.dateFormat(new Date(advice.createTime));
          advice.updateTime = dateFormat.dateFormat(new Date(advice.updateTime));
        })
      })
    },
    showUpdateAdvice() {
    },
    updateAdvice(){

    },
    deleteAdvice() {
    },
    handleCurrentPage(currentPage){
      //更新当前页码
      this.pageInfo.currentPage = currentPage
      if(this.searchCondition !== "" && this.searchCondition !== null){
        this.searchAdvice();
      }else{
        //访问服务器数据
        this.queryAllAdvice()
      }
    },
    searchAdvice(){

    },
    cleanSearch(){
      this.searchCondition = "";
      this.queryAllAdvice();
    }
  }
}
</script>

<style scoped>

</style>
