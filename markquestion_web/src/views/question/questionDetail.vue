<template>
    <el-row style="margin-top: 20px">
        <!--面包屑导航-->
        <el-breadcrumb separator="/" style="font-size: 16px;margin-left: 20px">
            <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
            <el-breadcrumb-item>题目:{{ this.question.questionName }}</el-breadcrumb-item>
        </el-breadcrumb>
        <el-col :span="16" :offset="4">
            <div style="margin: 20px auto;width: 1200px;border: 1px solid #c4ccc4;border-radius: 10px;padding: 10px;box-shadow: 0 0 5px #c4ccc4">
                <h1 style="display: inline-block">{{ this.question.questionName }}</h1>
                <br>
                <br>
                <div v-highlight style="font-size: 17px">
                    <div v-html="article"></div>
                </div>
                <br>
                <br>
                <br>
<!--                <h1>评论区</h1>-->
            </div>
        </el-col>
        <!--侧面题解信息-->
        <el-col :span="3" style="font-size: 18px;line-height: 30px;">
            <el-card class="box-card" style="margin-top: 35px">
                <div slot="header" class="clearfix">
                    <span>题解信息</span>
                    <el-button type="primary" size="mini" style="display: inline-block;float:right;">
                        <a href="" style="text-decoration: none;color: white">原题链接</a>
                    </el-button>
                </div>
                <div class="text item">
                    {{'作者:Autunomy'}}
                </div>
                <div class="text item">
                    {{'浏览量:999'}}
                </div>
                <div class="text item">
                    {{'算法标签:'}}
                    <el-tag
                        size="mini">
                        {{ 'DP' }}
                    </el-tag>
                </div>
                <div class="text item">
                    {{'难度:'}}
                    <el-tag
                        type="success"
                        size="mini"
                        effect="dark">
                        {{ '简单' }}
                    </el-tag>
                </div>
            </el-card>
        </el-col>
    </el-row>
</template>

<script>
import questionApi from "@/api/question";
import {marked} from "marked"

export default {
    name: "questionDetail",
    data() {
        return {
            qid: '',
            question: {},
            //题解主体
            article: ''
        }
    },
    mounted() {
        //获取参数 id
        this.qid = this.$route.params.id;
        questionApi.queryQuestionById(this.qid).then(resp => {
            let data = resp.data;
            if(data.code === 200){
                this.question = data.data;
                this.article = marked(this.question.solution);
            }
        })

    }
}
</script>

<style scoped>
</style>