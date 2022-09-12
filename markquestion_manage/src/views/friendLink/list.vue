<template>
  <div>
    <el-table
      :data="friendLinkList"
      border
      style="width: 100%;">
      <el-table-column
        fixed
        prop="linkName"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="link"
        label="链接"
        min-width="300px">
        <template v-slot="scope">
          <a :href="scope.row.link">{{scope.row.link}}</a>
        </template>
      </el-table-column>
      <el-table-column
        prop="tag"
        label="标签">
      </el-table-column>
      <el-table-column
        prop="introduce"
        label="简介">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建日期"
        min-width="200px">
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="修改日期"
        min-width="200px">
      </el-table-column>
      <el-table-column
        prop="description"
        label="介绍"
        min-width="300px">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="updateFriendLink(scope.row)">编辑</el-button>
          <el-button @click="deleteFriendLink(scope.row.id)" type="text" size="small" style="color: red">删除</el-button>
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
import friendLinkApi from "@/api/friendLink";
import qs from "qs";
import {marked} from "marked";
import dateFormat from "@/utils/dateFormat";

export default {
  name: "list",
  data() {
    return {
      pageInfo: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      friendLinkList: []
    }
  },
  mounted() {
    this.queryAllLink();
  },
  methods: {
    queryAllLink() {
      friendLinkApi.queryAllLink(qs.stringify(this.pageInfo)).then(resp => {
        if(resp.code === 200){
          this.pageInfo = resp.pageInfo
          this.friendLinkList = resp.data;
          this.friendLinkList.forEach(link => {
            link.createTime = dateFormat.dateFormat(new Date(link.createTime))
            link.updateTime = dateFormat.dateFormat(new Date(link.updateTime))
            if(link.description.length > 20){
              link.description = link.description.substring(0,19);
              link.description += "...";
            }
          })
        }
      })
    },
    handleCurrentPage(){

    },
    updateFriendLink(){},
    deleteFriendLink(){}
  }
}
</script>

<style scoped>

</style>
