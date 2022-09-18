<template>
  <div>
    <el-form :label-position="'right'" label-width="80px" :model="blog" style="width: 1000px;margin: 0 10px">
      <el-form-item style="width: 70%;margin-top: 10px;display: inline-block" label="博客标题">
        <el-input v-model="blog.title"></el-input>
      </el-form-item>
      <el-button style="display: inline-block;margin-left: 10px;" @click="toSubmit">提交</el-button>
    </el-form>
    <mavon-editor
      v-model="blog.content"
      @imgAdd="imgAdd"
      @imgDel="imgDel"
      :fontSize="'20'"
      style="min-height: 1000px;"
      ref=md
      :tab-size="4"></mavon-editor>

    <el-dialog title=" " :visible.sync="isShow" open="open">
      <el-form label-position="right" :model="blog" label-width="80px" v-show="isShow">
        <el-form-item label="分类">
          <el-select v-model="blog.className" placeholder="请选择">
            <el-option
              v-for="tag in blogClassList"
              :key="tag.className"
              :label="tag.className"
              :value="tag.className">
            </el-option>
          </el-select>
          <el-button style="margin-left: 20px" @click="manageClass">管理分类</el-button>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="blog.descp" style="display: inline-block;width: 500px"><span v-model="blog.descp"></span></el-input>
          <el-button @click="getDescp">一键提取</el-button>
        </el-form-item>
        <el-form-item style="text-align: center">
          <el-button type="primary" @click="addBlog">提交</el-button>
          <el-button @click="resetForm()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import blogApi from "@/api/blog";

export default {
  name: "add",
  data(){
    return {
      blog:{},
      blogClassList:[],
      isShow:false
    }
  },
  mounted() {
    this.queryAllClass();
  },
  methods:{
    addBlog(){},

    resetForm(){
      this.blog = {}
    },
    imgAdd(){},
    imgDel(){},
    toSubmit(){
      this.isShow = true;
    },
    manageClass(){},
    getDescp(){
      this.blog.descp = this.blog.content.substring(0,50);

    },
    queryAllClass(){
      blogApi.queryAllClass().then(resp => {
        this.blogClassList = resp.data;
      })
    }

  }
}
</script>

<style scoped>

</style>
