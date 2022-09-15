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
  data(){
    return {
      advice:{},
      adviceTagList:[],
      isShow:false,
      newTag:{}
    }
  },
  mounted() {
    this.getAdviceTagList();
  },
  methods:{
    getAdviceTagList(){
      adviceApi.getAdviceTagList().then(resp => {
        this.adviceTagList = resp.data;
      })
    },
    addTag(){

    },
    addAdvice(){},
    resetForm(){
      this.advice = {}
    },
    showDialog(){
      this.isShow = true;
    },
    manageTag(){}
  }
}
</script>

<style scoped>

</style>
