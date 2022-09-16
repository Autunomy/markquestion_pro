<template>
  <div>
    <el-table
      :data="adviceTagList"
      border
      style="width: 100%">
      <el-table-column
        fixed
        prop="id"
        label="id">
      </el-table-column>
      <el-table-column
        prop="adviceTagName"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="adviceTagNum"
        label="标签下的推荐数量">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="deleteAdviceTag(scope.row.id)" type="text" size="small" style="color: red">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import adviceApi from "@/api/advice";
import dateFormat from "@/utils/dateFormat";

export default {
  name: "adviceTagList",
  data(){
    return{
      adviceTagList:[]
    }
  },
  mounted() {
    this.getAdviceTagList()
  },
  methods:{
    getAdviceTagList(){
      adviceApi.getAdviceTagList().then(resp => {
        this.adviceTagList = resp.data;
        this.adviceTagList.forEach(adviceTag => {
          adviceTag.createTime = dateFormat.dateFormat(new Date(adviceTag.createTime));
        })
      })
    },
    deleteAdviceTag(id){
      //消息弹出框
      this.$confirm('是否要删除当前标签(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adviceApi.deleteAdviceTag(id).then(resp => {
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
          this.getAdviceTagList();
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
