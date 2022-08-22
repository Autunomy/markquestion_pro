<template>
  <div>
    <el-form label-position="right" label-width="50px">
      <el-form-item style="width: 50%;margin-top: 10px;display: inline-block" label="标题">
        <el-input v-model="question.questionName" placeholder="题解标题"></el-input>
      </el-form-item>
      <el-button style="display: inline-block;margin-left: 10px;" type="primary" @click="submitSolution">更新题解</el-button>
    </el-form>
    <mavon-editor
      v-model="question.solution"
      @imgAdd="imgAdd"
      @imgDel="imgDel"
      :fontSize="'20'"
      style="min-height: 1000px;"
      ref=md
      :tab-size="4"></mavon-editor>

    <el-dialog title=" " :visible.sync="doSubmit">
      <el-form label-position="right" :model="question" >
        <el-form-item label="作者" style="margin: 0 0">
          <el-input v-model="question.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="原题链接" style="margin: 0 0">
          <el-input v-model="question.link" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="题目来源" style="display: inline-block;margin: 0 30px 0 0">
          <el-select v-model="question.questionFrom" placeholder="请选择题目来源">
            <el-option v-for="platform in platforms" :label="platform.platform" :value="platform.platform"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="语言标签" style="display: inline-block;margin: 0 30px 0 0">
          <el-select v-model="question.tag" placeholder="请选择语言标签">
            <el-option label="java" value="java"></el-option>
            <el-option label="cpp" value="cpp"></el-option>
            <el-option label="python3" value="python3"></el-option>
            <el-option label="js" value="js"></el-option>
            <el-option label="go" value="go"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="难度" style="display: inline-block;margin: 0 30px 0 0">
          <el-select v-model="question.level" placeholder="请选择难度">
            <el-option label="简单" value="简单"></el-option>
            <el-option label="中等" value="中等"></el-option>
            <el-option label="困难" value="困难"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="doSubmit = false">取 消</el-button>
        <el-button type="primary" @click="submit">更 新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import questionApi from "@/api/question";
import qs from "qs";

export default {
  name: "updateQuestion",
  data(){
    return{
      question:{
        questionName:"",
        solution:"",
        questionFrom:"",
        author:"Autunomy",
        tag:"",
        level:"",
        link:""
      },
      doSubmit:false,
      platforms:[]
    }
  },
  mounted() {
    if(this.$route.fullPath.lastIndexOf('?') === -1){
      this.$message.warning({
        message:"请从题解列表跳转至修改页面",
        duration:2000
      })
      this.$router.push("/question/list")
    }else{
      let id = this.$route.fullPath.substring(this.$route.fullPath.lastIndexOf('=')+1);
      //根据id查询题解信息
      questionApi.queryQuestionById(id).then(resp => {
        this.question = resp.data;
      })
    }
  },
  methods:{
    // 图片上传
    imgAdd(pos, $file) {
      // 第一步.将图片上传到服务器.
      let data = new FormData();
      data.append('image', $file);
      questionApi.uploadPic(data).then(resp => {
        //设置图片地址
        this.$refs.md.$img2Url(pos,resp)
      })
    },
    //上传图片删除
    imgDel(pos){
      questionApi.delPic(pos[0]).then(resp => {})
    },
    //点击发布题解按钮  弹出对话框
    submitSolution(){
      this.doSubmit = true;
      //获取题目来源
      this.getQuestionFrom();
    },
    getQuestionFrom(){
      questionApi.getQuestionFrom().then(resp => {
        this.platforms = resp.data
      })
    },
    //在弹出框中点击提交
    submit(){
      questionApi.updateQuestion(qs.stringify(this.question)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"更新成功",
            duration:2000
          })
        }else{
          this.$message.success({
            message:"更新失败",
            duration:2000
          })
        }
      })
      //路由跳转
      this.$router.push("/question/list")
    }
  }
}
</script>

<style scoped>

</style>
