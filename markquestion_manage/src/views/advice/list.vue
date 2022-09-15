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

    <!--修改页面-->
    <el-dialog title="修改推荐信息" :visible.sync="isShow" top="20px">
      <el-form :model="advice">
        <el-form-item label="推荐名称">
          <el-input v-model="advice.adviceName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="推荐作者">
          <el-input v-model="advice.adviceAuthor" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="推荐标签">
          <el-select v-model="advice.adviceTag" placeholder="请选择">
            <el-option
              v-for="adviceTag in adviceTagList"
              :key="adviceTag.adviceTagName"
              :label="adviceTag.adviceTagName"
              :value="adviceTag.adviceTagName">
            </el-option>
          </el-select>
          <el-button style="margin-left: 20px" @click="manageTag">管理标签</el-button>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="advice.adviceDescribe" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="详细信息">
          <el-input v-model="advice.adviceContent" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="上传图片" >
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8001/advice/uploadImg"
            :show-file-list="false"
            :on-success="handleAvatarSuccess">
            <img v-if="imageUrl" :src="imageUrl" class="avatar" >
            <i v-else class="el-icon-plus avatar-uploader-icon" style="border: 1px solid gray"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="updateAdvice">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import adviceApi from "@/api/advice";
import qs from "qs";
import dateFormat from "@/utils/dateFormat";
import friendLinkApi from "@/api/friendLink";

export default {
  name: "list",
  data() {
    return {
      adviceList: [],
      adviceTagList:[],
      pageInfo: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      searchCondition:"",
      isShow:false,
      advice:{},
      imageUrl:""
    }
  },
  mounted() {
    this.queryAllAdvice();
  },
  methods: {
    getAdviceTagList() {
      adviceApi.getAdviceTagList().then(resp => {
        this.adviceTagList = resp.data;
      })
    },
    queryAllAdvice() {
      adviceApi.queryAllAdvice(qs.stringify(this.pageInfo)).then(resp => {
        this.adviceList = resp.data;
        this.adviceList.forEach(advice => {
          advice.adviceImg = "http://localhost:8001" + advice.adviceImg;
          advice.createTime = dateFormat.dateFormat(new Date(advice.createTime));
          advice.updateTime = dateFormat.dateFormat(new Date(advice.updateTime));
        })
      })
    },
    showUpdateAdvice(advice) {
      this.isShow = true;
      this.getAdviceTagList();
      this.advice = advice;
      this.imageUrl = advice.adviceImg;
    },
    updateAdvice(){
      adviceApi.updateAdvice(qs.stringify(this.advice)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"修改成功",
            duration:1500
          })
        }else{
          this.$message.error({
            message:"修改失败",
            duration:1500
          })
        }
        this.isShow = false;
        this.queryAllAdvice();
      })

    },
    cancel(){
      this.isShow = false;
      adviceApi.deleteImg(this.advice.adviceImg);
    },
    deleteAdvice(id) {
      //消息弹出框
      this.$confirm('是否要删除当前推荐(删除将无法找回)?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adviceApi.deleteAdvice(id).then(resp => {
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
          if (this.adviceList.length === 1 && this.pageInfo.currentPage > 1) {
            this.pageInfo.currentPage--;
          }
          this.queryAllAdvice();
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
    },
    handleAvatarSuccess(response){
      this.imageUrl = "http://localhost:8001/images/advice/"+response.data;
      this.advice.adviceImg = response.data;
    },
    manageTag(){

    }
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
