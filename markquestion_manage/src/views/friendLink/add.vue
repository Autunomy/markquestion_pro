<template>
  <div>
    <el-form :label-position="'right'" label-width="100px" :model="friendLink" style="width: 600px;margin: 30px auto">
      <el-form-item label="友链名称">
        <el-input v-model="friendLink.linkName"></el-input>
      </el-form-item>
      <el-form-item label="友链地址">
        <el-input v-model="friendLink.link"></el-input>
      </el-form-item>
      <el-form-item label="友链标签">
        <el-select v-model="friendLink.tag" placeholder="请选择">
          <el-option
            v-for="tag in friendLinkTag"
            :key="tag.name"
            :label="tag.name"
            :value="tag.name">
          </el-option>
        </el-select>
        <el-button style="margin-left: 20px" @click="showDialog">添加标签</el-button>
        <el-button style="margin-left: 20px" @click="manageTag">管理标签</el-button>
      </el-form-item>
      <el-form-item label="友链简介">
        <el-input v-model="friendLink.introduce" placeholder="简短的介绍即可"></el-input>
      </el-form-item>
      <el-form-item label="友链详细描述">
        <el-input type="textarea" v-model="friendLink.description" placeholder="支持markdown语法"></el-input>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button type="primary" @click="addFriendLink()">提交</el-button>
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
import friendLinkApi from "@/api/friendLink";
import qs from "qs";

export default {
  name: "add",
  data() {
    return {
      friendLink: {},
      friendLinkTag: [],
      newTag:{},
      isShow:false
    }
  },
  mounted() {
    this.queryFriendLinkTag();
  },
  methods: {
    addFriendLink() {
      friendLinkApi.addFriendLink(qs.stringify(this.friendLink)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"添加成功",
            duration:1500
          })
          this.$router.push("/friendLink/list")
        }else{
          this.$message.warning({
            message:"添加失败",
            duration:1500
          })
        }
      })
    },
    queryFriendLinkTag() {
      friendLinkApi.queryFriendLinkTag().then(resp => {
        this.friendLinkTag = resp.data;
      })
    },
    resetForm() {
      this.friendLink = {}
    },
    showDialog(){
      this.isShow = true;
    },
    addTag(){
      friendLinkApi.addFriendLinkTag(qs.stringify(this.newTag)).then(resp => {
        if(resp.code === 200){
          this.$message.success({
            message:"添加成功",
            duration:1500
          })
          this.queryFriendLinkTag();
        }else{
          this.$message.error({
            message:"添加失败",
            duration:1500
          })
        }
        //关闭显示
        this.isShow = false;
      })
    },
    manageTag(){
      this.$router.push("/friendLink/tagList");
    }
  }
}
</script>

<style scoped>

</style>
