<template>
  <div>
    <el-table
      :data="contestList"
      border
      style="width: 100%;">
      <el-table-column
        fixed
        prop="id"
        label="编号">
      </el-table-column>
      <el-table-column
        prop="contestName"
        label="比赛名称">
      </el-table-column>
      <el-table-column
        prop="contestTime"
        label="比赛日期">
      </el-table-column>
      <el-table-column
        prop="platform"
        label="平台">
      </el-table-column>
      <el-table-column
        prop="link"
        label="地址链接">
        <template v-slot="scope">
          <a style="color: #409EFF" :href="scope.row.link">{{scope.row.link}}</a>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showUpdateContest(scope.row)">编辑</el-button>
          <el-button @click="deleteContest(scope.row.id)" type="text" size="small" style="color: red">删除</el-button>
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
    <el-dialog title="修改比赛" :visible.sync="isShow" top="20px">
      <el-form :model="contest">
        <el-form-item label="比赛平台">
          <el-select v-model="contest.platform" placeholder="请选择">
            <el-option
              v-for="platform in platformList"
              :key="platform.platform"
              :label="platform.platform"
              :value="platform.platform">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="比赛链接">
          <el-input v-model="contest.link" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="比赛日期">
          <div class="block">
            <el-date-picker
              v-model="contest.contestTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期时间">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="比赛名称">
          <el-input v-model="contest.contestName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="this.isShow = false">取 消</el-button>
        <el-button type="primary" @click="updateContest">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import contestApi from "@/api/contest";
import qs from "qs";
import dateFormat from "@/utils/dateFormat";
import questionApi from "@/api/question";

export default {
  name: "contestList",
  data() {
    return {
      contestList: [],
      pageInfo: {
        currentPage:1,
        pageSize:10,
        total:0
      },
      isShow:false,
      contest:{},
      platformList:[]
    }
  },
  mounted() {
    this.queryContestPage();
  },
  methods:{
    queryContestPage(){
      contestApi.queryContestPage(qs.stringify(this.pageInfo)).then(resp => {
        this.contestList = resp.data;
        this.contestList.forEach(contest => {
          contest.contestTime = dateFormat.dateFormat(new Date(contest.contestTime));
        })
        this.pageInfo = resp.pageInfo;
      })
    },
    deleteContest(id){
      //消息弹出框
      this.$confirm('是否要删除当前比赛(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        contestApi.deleteContestById(id).then(resp => {
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
          if (this.contestList.length === 1 && this.pageInfo.currentPage > 1) {
            this.pageInfo.currentPage--;
          }
          this.queryContestPage();
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
    showUpdateContest(data){
      this.isShow = true;
      this.contest = data
      //获取比赛平台
      questionApi.getQuestionFrom().then(resp => {
        this.platformList = resp.data;
      })
    },
    updateContest(){
      contestApi.updateContest(qs.stringify(this.contest)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"修改成功",
            duration:1500
          })
          this.queryContestPage();
        }else{
          this.$message.error({
            message:"修改失败",
            duration:1500
          })
        }
      })
      this.isShow = false;
    },
    handleCurrentPage(currentPage){
      //更新当前页码
      this.pageInfo.currentPage = currentPage
      //访问服务器数据
      this.queryAllLink()
    }
  }
}
</script>

<style scoped>

</style>
