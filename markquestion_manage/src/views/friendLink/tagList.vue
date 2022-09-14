<template>
  <div>
    <el-table
      :data="friendLinkTagList"
      border
      style="width: 100%">
      <el-table-column
        fixed
        prop="id"
        label="id">
      </el-table-column>
      <el-table-column
        prop="name"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="最后一次修改时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="deleteFriendLinkTag(scope.row.id)" type="text" size="small" style="color: red">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import friendLinkApi from "@/api/friendLink";
import dateFormat from "@/utils/dateFormat";

export default {
  name: "tagList",
  data(){
    return{
      friendLinkTagList:[]
    }
  },
  mounted() {
    this.queryAllFriendLinkTag();
  },
  methods:{
    queryAllFriendLinkTag(){
      friendLinkApi.queryAllFriendLinkTag().then(resp => {
        this.friendLinkTagList = resp.data;
        this.friendLinkTagList.forEach(tag => {
          tag.createTime = dateFormat.dateFormat(new Date(tag.createTime));
          tag.updateTime = dateFormat.dateFormat(new Date(tag.updateTime));
        })
      })
    },
    deleteFriendLinkTag(id){
      //消息弹出框
      this.$confirm('是否要删除当前友链标签(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        friendLinkApi.deleteFriendLinkTag(id).then(resp => {
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
          this.queryAllFriendLinkTag();
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
    }
  }

}
</script>

<style scoped>

</style>
