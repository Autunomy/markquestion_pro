<template>
<div>
  <el-form :label-position="'right'" label-width="80px" :model="contest" style="width: 600px;margin: 30px auto">
    <el-form-item label="比赛名称">
      <el-input v-model="contest.contestName"></el-input>
    </el-form-item>
    <el-form-item label="比赛时间">
      <div class="block">
        <el-date-picker
          v-model="contest.contestTime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期时间">
        </el-date-picker>
      </div>
    </el-form-item>
    <el-form-item label="平台">
      <el-select v-model="contest.platform" placeholder="请选择平台">
        <el-option v-for="platform in platformList" :label="platform.platform" :value="platform.platform"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="链接">
      <el-input v-model="contest.link"></el-input>
    </el-form-item>
    <el-form-item style="text-align: center">
      <el-button type="primary" @click="addContest">提交</el-button>
      <el-button @click="resetForm()">重置</el-button>
    </el-form-item>
  </el-form>
</div>
</template>

<script>
import questionApi from "@/api/question";
import contestApi from "@/api/contest";
import qs from "qs";

export default {
  name: "addContest",
  data(){
    return{
      contest:{
        contestName:"",
        contestTime:"",
        platform:"",
        link:""
      },
      platformList:[]
    }
  },
  mounted() {
    this.getPlatform();
  },
  methods:{
    getPlatform(){
      questionApi.getQuestionFrom().then(resp => {
        this.platformList = resp.data;
      })
    },
    resetForm(){
      this.contest = {}
    },
    addContest(){
      contestApi.addContest(qs.stringify(this.contest)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"添加成功",
            duration:2000
          })
        }else{
          this.$message.success({
            message:"添加失败",
            duration:2000
          })
        }
        this.$router.push("/contest/contestList");
      })
    }
  }
}
</script>

<style scoped>

</style>
