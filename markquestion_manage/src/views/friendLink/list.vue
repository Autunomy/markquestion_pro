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
          <el-button style="margin-left: 20px" type="primary" @click="searchFriendLink">搜索</el-button>
        </el-form-item>
        <el-button style="margin-left: 20px" type="info" @click="cleanSearch">清空搜索条件</el-button>
      </el-form>
    </div>

    <el-table
      :data="friendLinkList"
      border
      style="width: 100%">
      <el-table-column
        fixed
        prop="id"
        label="id">
      </el-table-column>
      <el-table-column
        prop="linkName"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="link"
        label="链接"
        min-width="300px">
        <template v-slot="scope">
          <a :href="scope.row.link" style="color: lightskyblue">{{ scope.row.link }}</a>
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
          <el-button type="text" size="small" @click="showUpdateFriendLink(scope.row)">编辑</el-button>
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

    <!--修改页面-->
    <el-dialog title="修改友链" :visible.sync="isShow" top="20px">
      <el-form :model="friendLink">
        <el-form-item label="名称">
          <el-input v-model="friendLink.linkName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="链接">
          <el-input v-model="friendLink.link" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="friendLink.tag" placeholder="请选择">
            <el-option
              v-for="tag in friendLinkTagList"
              :key="tag.name"
              :label="tag.name"
              :value="tag.name">
            </el-option>
          </el-select>
          <el-button style="margin-left: 20px" @click="manageTag">管理标签</el-button>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="friendLink.introduce" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="详细描述">
          <el-input type="textarea" v-model="friendLink.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="this.isShow = false">取 消</el-button>
        <el-button type="primary" @click="updateFriendLink">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import friendLinkApi from "@/api/friendLink";
import qs from "qs";
import {marked} from "marked";
import dateFormat from "@/utils/dateFormat";
import questionApi from "@/api/question";

export default {
  name: "list",
  data() {
    return {
      pageInfo: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      friendLinkList: [],
      friendLink:{},
      friendLinkTagList:[],
      searchCondition:"",
      isShow:false
    }
  },
  mounted() {
    this.queryAllLink();
  },
  methods: {
    queryAllLink() {
      friendLinkApi.queryAllLink(qs.stringify(this.pageInfo)).then(resp => {
        if (resp.code === 200) {
          this.pageInfo = resp.pageInfo
          this.friendLinkList = resp.data;
          this.friendLinkList.forEach(link => {
            link.createTime = dateFormat.dateFormat(new Date(link.createTime))
            link.updateTime = dateFormat.dateFormat(new Date(link.updateTime))
            // if (link.description.length > 20) {
            //   link.description = link.description.substring(0, 19);
            //   link.description += "...";
            // }
          })
        }
      })
    },
    queryFriendLinkTag() {
      friendLinkApi.queryFriendLinkTag().then(resp => {
        this.friendLinkTagList = resp.data;
      })
    },
    handleCurrentPage(currentPage) {
      //更新当前页码
      this.pageInfo.currentPage = currentPage
      if(this.searchCondition !== "" && this.searchCondition !== null){
        this.searchFriendLink();
      }else{
        //访问服务器数据
        this.queryAllLink()
      }
    },
    showUpdateFriendLink(data) {
      this.isShow = true;
      this.friendLink = data;
      this.queryFriendLinkTag()
    },
    updateFriendLink(){
      console.log(qs.stringify(this.friendLink))
      friendLinkApi.updateFriendLink(qs.stringify(this.friendLink)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"修改成功",
            duration:1500
          })
          this.queryAllLink();
        }else{
          this.$message.error({
            message:"修改失败",
            duration:1500
          })
        }
      })
      this.isShow = false;
    },
    deleteFriendLink(id) {
      //消息弹出框
      this.$confirm('是否要删除当前友链(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        friendLinkApi.deleteFriendLinkById(id).then(resp => {
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
          if (this.friendLinkList.length === 1 && this.pageInfo.currentPage > 1) {
            this.pageInfo.currentPage--;
          }
          this.queryAllLink();
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
    searchFriendLink(){
      this.pageInfo.currentPage = 1
      let str = qs.stringify(this.pageInfo);
      str = "searchCondition="+this.searchCondition + "&" +str;
      friendLinkApi.searchFriendLinkByName(str).then(resp => {
        this.friendLinkList = resp.data;
        this.pageInfo = resp.pageInfo;
        this.friendLinkList.forEach(link => {
          link.createTime = dateFormat.dateFormat(new Date(link.createTime))
          link.updateTime = dateFormat.dateFormat(new Date(link.updateTime))
          // if (link.description.length > 20) {
          //   link.description = link.description.substring(0, 19);
          //   link.description += "...";
          // }
        })
      })
    },
    cleanSearch(){
      this.searchCondition = "";
      this.queryAllLink();
    },
    manageTag(){
      this.$router.push("/friendLink/tagList");
    }
  }
}
</script>

<style scoped>

</style>
