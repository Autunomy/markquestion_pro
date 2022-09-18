<template>
    <el-row style="margin-bottom: 20px">
        <div style="margin-top: 20px">
            <!--分类专栏-->
            <el-col :span="4" :offset="3">
                <el-card>
                    <hr style="margin: 0 0">
                    <div v-for="aClass in classList">
                        <a @click="changeBlogClass" href="Javascript:void(0)" style="color: black">
                            <el-row>
                                <el-col :span="2">
                                    <h4>{{ aClass.className }}</h4>
                                </el-col>
                                <el-col :span="6" :offset="16">
                                    <h5 style="margin-top: 12px">{{ aClass.classNum }}篇</h5>
                                </el-col>
                            </el-row>
                        </a>
                        <hr style="margin: 0 0">
                    </div>
                </el-card>
            </el-col>
            <el-col :span="14">
                <div v-for="blog in blogList">
                    <router-link :to="'/blogContent/'+blog.id"
                       style="text-decoration: none;display: inline-block;margin-bottom: 5px;width: 100%">
                        <el-card style="margin-left: 6px">
                            <h3 style="margin: 0 0">{{ blog.title }}</h3>
                            <p style="margin-top: 6px;color: #555b6b">{{ blog.content }}</p>
                            <i class="el-icon-time">{{blog.createTime}}</i>
                            &nbsp;&nbsp;-&nbsp;&nbsp;
                            <span>阅读量:{{blog.watch}}</span>
                            &nbsp;&nbsp;-&nbsp;&nbsp;
                            <span>评论:{{blog.commentNum}}</span>
                        </el-card>
                    </router-link>
                </div>
            </el-col>
        </div>
    </el-row>
</template>

<script>
import blogApi from "@/api/blog";
import qs from "qs";
import dateFormat from "@/utils/dateFormat";

export default {
    name: "index",
    data() {
        return {
            blogList: [],
            classList:[],
            pageInfo: {
                currentPage: 1,
                pageSize: 5,
                total: 0
            }
        }
    },
    mounted() {
        this.queryAllBlog();
        this.queryAllClass();
    },
    methods: {
        //点击分类之后可以切换分类
        changeBlogClass() {

        },
        //获取所有的博客
        queryAllBlog() {
            blogApi.queryAllBlog(qs.stringify(this.pageInfo)).then(resp => {
                resp = resp.data;
                if (resp.code === 200) {
                    this.blogList = resp.data;
                    this.pageInfo = resp.pageInfo;
                    this.blogList.forEach(blog => {
                        //格式化日期时间
                        blog.createTime = dateFormat.dateFormat(new Date(blog.createTime));
                    })

                } else {
                    this.$message.warning("获取博客列表失败")
                }
            }).catch(error => {
                this.$message.error("获取失败-" + error)
            })
        },
        //获取所有分类
        queryAllClass(){
            blogApi.queryAllClass().then(resp => {
                this.classList = resp.data.data;
            })
        }
    }
}
</script>

<style scoped>

</style>