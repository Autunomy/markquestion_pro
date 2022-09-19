<template>
  <div>
    <el-form :label-position="'right'" label-width="100px" :model="advice" style="width: 600px;margin: 30px auto">
      <el-form-item label="推荐名称">
        <el-input v-model="advice.adviceName"></el-input>
      </el-form-item>
      <el-form-item label="推荐作者">
        <el-input v-model="advice.author"></el-input>
      </el-form-item>
      <el-form-item label="推荐标签">
        <el-select v-model="advice.adviceTag" placeholder="请选择">
          <el-option
            v-for="tag in adviceTagList"
            :key="tag.adviceTagName"
            :label="tag.adviceTagName"
            :value="tag.adviceTagName">
          </el-option>
        </el-select>
        <el-button style="margin-left: 20px" @click="showDialog">添加标签</el-button>
        <el-button style="margin-left: 20px" @click="manageTag">管理标签</el-button>
      </el-form-item>
      <el-form-item label="推荐简介">
        <el-input v-model="advice.adviceDescription" placeholder="简短的介绍即可"></el-input>
      </el-form-item>
      <el-form-item label="推荐描述">
        <el-input type="textarea" v-model="advice.adviceContent" placeholder="支持markdown语法"></el-input>
      </el-form-item>
      <el-form-item label="上传图片" >
        <el-upload
          class="avatar-uploader"
          action="http://180.76.97.59:8001/advice/uploadImg"
          :show-file-list="false"
          :on-success="handleAvatarSuccess">
          <img v-if="imageUrl" :src="imageUrl" class="avatar" >
          <i v-else class="el-icon-plus avatar-uploader-icon" style="border: 1px solid gray"></i>
        </el-upload>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button type="primary" @click="addAdvice()">提交</el-button>
        <el-button @click="resetForm()">重置</el-button>
      </el-form-item>
    </el-form>

    <!--弹出层 为了添加标签-->
    <el-dialog title="添加标签" :visible.sync="isShow">
      <el-form :model="newTag">
        <el-form-item label="标签名称">
          <el-input v-model="newTag.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShow = false">取 消</el-button>
        <el-button type="primary" @click="addTag">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import adviceApi from "@/api/advice";
import qs from "qs";

export default {
  name: "add",
  data() {
    return {
      advice: {},
      adviceTagList: [],
      isShow: false,
      newTag: {},
      imageUrl:""
    }
  },
  mounted() {
    this.getAdviceTagList();
  },
  methods: {
    getAdviceTagList() {
      adviceApi.getAdviceTagList().then(resp => {
        this.adviceTagList = resp.data;
      })
    },
    addTag() {
      adviceApi.addTag(this.newTag.name).then(resp => {
        console.log(resp)
        if(resp.code === 200){
          this.$message.success({
            message:"添加成功",
            duration:1500
          })
          this.getAdviceTagList()
        }else{
          this.$message.error({
            message:"添加失败",
            duration:1500
          })
        }
      })
      this.isShow = false
    },
    addAdvice() {
      console.log(qs.stringify(this.advice))
      adviceApi.addAdvice(qs.stringify(this.advice)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"添加成功",
            duration:1500
          })
          this.$router.push("/advice/list")
        }else{
          this.$message.error({
            message:"添加失败",
            duration:1500
          })
          this.resetForm();
        }
      })
    },
    resetForm() {
      //先删除图片
      this.deleteImg();
      //将信息清空
      this.advice = {}
    },
    showDialog() {
      this.isShow = true;
    },
    manageTag() {
    },
    handleAvatarSuccess(response){
      this.imageUrl = "http://180.76.97.59:8001/images/advice/"+response.data;
      this.advice.adviceImg = response.data;
    },
    //删除图片
    deleteImg(){
      adviceApi.deleteImg(this.advice.adviceImg);
      this.imageUrl = ""
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
