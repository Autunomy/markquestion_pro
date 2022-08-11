<template>
    <el-row style="margin-top: 20px">
        <!--面包屑导航-->
        <el-breadcrumb separator="/" style="font-size: 16px;margin-left: 20px">
            <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
            <el-breadcrumb-item>题目:{{ this.question.questionName }}</el-breadcrumb-item>
        </el-breadcrumb>
        <!--题解显示区域-->
        <el-col :span="15" :offset="4">
            <div
                style="margin: 20px auto;width: 100%;border: 1px solid #c4ccc4;border-radius: 10px;box-shadow: 0 0 5px #c4ccc4">
                <div style="margin: 10px 10px">
                    <!--题解题目-->
                    <h1 style="display: inline-block">{{ this.question.questionName }}</h1>
                    <br>
                    <br>
                    <!--内容部分-->
                    <div v-highlight style="font-size: 17px">
                        <div v-html="article"></div>
                    </div>
                </div>

                <el-divider></el-divider>

                <!--评论框-->
                <div>
                    <h1 style="margin-left: 10px">评论区</h1>
                    <div style="border: 3px solid #c4ccc4;border-radius: 10px;box-shadow: 0 0 5px #c4ccc4;margin: 5px">
                        <mavon-editor
                            placeholder="在这里写下评论，支持markdown语法"
                            :toolbarsFlag="false"
                            style="min-height: 100px;"
                            v-model="comment.content"
                            :subfield="false"></mavon-editor>
                    </div>
                    <div style="margin-left: 5px;">
                        <el-button
                            style="margin-left: 89%"
                            @click="saveComment"
                            type="primary">发布评论</el-button>
                    </div>
                </div>

                <!--评论展示部分-->
                <div v-for="acomment in commentList" style="margin-left: 10px;margin-right: 10px">
                    <el-divider></el-divider>
                    <el-row>
                        <!--用户头像-->
                        <el-col :span="1" style="margin-right: 20px">
                            <el-avatar style="display: inline-block;" :size="50" :src="'http://www.autunomy.top/images/head/head.jpg'"/>
                        </el-col>
                        <el-col :span="20">
                            <!--第一条评论信息-->
                            <div>
                                <!--显示作者-->
                                <h4 style="display: inline-block;margin-right: 20px">{{'Autunomy'}}</h4>
                                <!--显示评论日期-->
                                <h4 style="display: inline-block;margin-right: 20px">{{acomment.commentDate}}</h4>                            <!--显示作者-->
                                <!--回复按钮-->
                                <h4 style="display: inline-block">
                                    <!--点击之后需要将当前回复框展示出来 id就是当前选中的评论的id-->
                                    <a href="JavaScript:void (0)"
                                       @click="changeReply(acomment.id)"
                                       style="color: #848c84">回复</a>
                                </h4>
                                <!--评论展示部分-->
                                <div v-highlight>
                                    <div v-html="acomment.content"></div>
                                </div>

                                <!--回复的评论展示部分-->
<!--                                <div>-->
<!--                                    <div v-for="rcomment in acomment.replyList">-->
<!--                                        &lt;!&ndash;显示作者&ndash;&gt;-->
<!--                                        <h4 style="display: inline-block;margin-right: 20px">{{'Autunomy'}} 回复 {{rcomment.userId}}</h4>-->
<!--                                        &lt;!&ndash;显示评论日期&ndash;&gt;-->
<!--                                        <h4 style="display: inline-block;margin-right: 20px">{{rcomment.commentDate}}</h4>                            &lt;!&ndash;显示作者&ndash;&gt;-->
<!--                                        &lt;!&ndash;回复按钮&ndash;&gt;-->
<!--                                        <h4 style="display: inline-block">-->
<!--                                            &lt;!&ndash;点击之后需要将当前回复框展示出来 id就是当前选中的评论的id&ndash;&gt;-->
<!--                                            <a href="JavaScript:void (0)"-->
<!--                                               @click="changeReply(rcomment.id)"-->
<!--                                               style="color: #848c84">回复</a>-->
<!--                                        </h4>-->
<!--                                        &lt;!&ndash;评论展示部分&ndash;&gt;-->
<!--                                        <div v-highlight>-->
<!--                                            <div v-html="rcomment.content"></div>-->
<!--                                        </div>-->

<!--                                        &lt;!&ndash;回复框&ndash;&gt;-->
<!--                                        <transition name="el-zoom-in-top">-->
<!--                                            <div v-show="reply == rcomment.id">-->
<!--                                                <div style="border: 3px solid #c4ccc4;border-radius: 10px;box-shadow: 0 0 5px #c4ccc4;">-->
<!--                                                    <mavon-editor-->
<!--                                                        placeholder="在这里写下评论，支持markdown语法"-->
<!--                                                        :toolbarsFlag="false"-->
<!--                                                        style="min-height: 100px;"-->
<!--                                                        v-model="comment.content"-->
<!--                                                        :subfield="false"></mavon-editor>-->
<!--                                                </div>-->
<!--                                                <div style="margin-left: 5px;">-->
<!--                                                    <a-->
<!--                                                        href="JavaScript:void (0)"-->
<!--                                                        style="margin-left: 92%"-->
<!--                                                        @click="replyComment(rcomment.id)"-->
<!--                                                        type="primary">回复评论</a>-->
<!--                                                </div>-->
<!--                                            </div>-->
<!--                                        </transition>-->

<!--                                    </div>-->
<!--                                </div>-->

                                <!--回复框-->
                                <transition name="el-zoom-in-top">
                                    <div v-show="reply == acomment.id">
                                        <div style="border: 3px solid #c4ccc4;border-radius: 10px;box-shadow: 0 0 5px #c4ccc4;">
                                            <mavon-editor
                                                placeholder="在这里写下评论，支持markdown语法"
                                                :toolbarsFlag="false"
                                                style="min-height: 100px;"
                                                v-model="comment.content"
                                                :subfield="false"></mavon-editor>
                                        </div>
                                        <div style="margin-left: 5px;">
                                            <a
                                                href="JavaScript:void (0)"
                                                style="margin-left: 92%"
                                                @click="replyComment(acomment.id)"
                                                type="primary">回复评论</a>
                                        </div>
                                    </div>
                                </transition>
                            </div>
                        </el-col>
                    </el-row>
                </div>

                <!--占位-->
                <div style="margin-bottom: 20px"></div>
            </div>
        </el-col>
        <!--侧面题解信息-->
        <el-col :span="4" style="font-size: 18px;line-height: 30px;margin-left: 10px">
            <el-card class="box-card" style="margin-top: 35px">
                <div slot="header" class="clearfix">
                    <span>题解信息</span>
                    <el-button type="primary" size="mini" style="display: inline-block;float:right;">
                        <a href="" style="text-decoration: none;color: white">原题链接</a>
                    </el-button>
                </div>
                <div class="text item">
                    {{ '作者:' + question.author }}
                </div>
                <div class="text item">
                    {{ '浏览量:' + question.watch }}
                </div>
                <div class="text item">
                    {{ '算法标签:' }}
                    <el-tag
                        size="mini">
                        {{ 'DP' }}
                    </el-tag>
                </div>
                <div class="text item">
                    {{ '难度:' }}
                    <el-tag
                        :type="question.level === '简单' ? 'success' : (question.level === '中等' ? 'warning' : 'danger')"
                        size="mini"
                        effect="dark">
                        {{ question.level }}
                    </el-tag>
                </div>
            </el-card>
        </el-col>
    </el-row>
</template>

<script>
import questionApi from "@/api/question";
import commentApi from "@/api/comment";
import dateFormat from "@/utils/dateFormat";
import qs from "qs";
import {marked} from "marked"
import Vue from "vue";

export default {
    name: "questionDetail",
    data() {
        return {
            //当前题解的id  用来做查询
            qid: '',
            //当前题解的所有信息
            question: {},
            //题解主体
            article: '',
            //哪个评论对应的回复框需要展示  按照id进行判断
            reply:-1,
            //题解对的评论列表
            commentList:[],
            //需要提交的评论信息
            comment:{
                content:'',
                qid:0,
                uid:1,
                commentId:0
            },
            //中间变量
            replyList:[]
        }
    },
    mounted() {
        //渲染题解
        this.queryQuestionById();
    },
    methods: {
        //获取题解信息
        queryQuestionById() {
            //获取参数 id
            this.qid = this.$route.params.id;
            this.comment.qid = this.$route.params.id;
            questionApi.queryQuestionById(this.qid).then(resp => {
                let data = resp.data;
                if (data.code === 200) {
                    this.question = data.data;
                    this.article = marked(this.question.solution);
                    //阅读量+1
                    this.addWatch(this.question.id);
                    //获取评论
                    this.queryAllCommentByQuestionId(this.qid);
                }
            })
        },
        //将浏览量+1
        addWatch(id) {
            //只有当前用户没有浏览过才+1 访问之后就将访问信息存储进去
            if (!sessionStorage.getItem(id)) {
                questionApi.addWatch(id);
                //储存在sessionStorage中
                sessionStorage.setItem(id, "hasWatch");
            }
        },
        //展示评论对应的回复框
        changeReply(id){
            //当前展示框已经展示出来 那就让其消失
            if(this.reply === id){
                this.reply = -1;
            }else{
                this.reply = id;
            }
        },
        //保存评论
        saveComment(){
            if(this.comment.content !== "" && this.content !== null){
                // console.log(qs.stringify(this.comment))
                commentApi.saveComment(qs.stringify(this.comment)).then(resp => {
                    if(resp.data.code === 200){
                        this.$message.success({
                            message:"评论成功",
                            duration:1500
                        })
                        //评论框清空
                        this.comment.content = "";
                        //重新获取评论
                        this.queryAllCommentByQuestionId(this.qid);
                    }else{
                        this.$message.warning({
                            message:"评论失败",
                            duration:1500
                        })
                    }
                }).catch(error => {
                    this.$message.error({
                        message:error,
                        duration:1500
                    })
                })
            }else{
                this.$message.warning({
                    message:"评论不能为空",
                    duration:1500
                })
            }
        },
        //获取评论列表
        queryAllCommentByQuestionId(qid){
            commentApi.queryAllCommentByQuestionId(qid).then(resp => {
                let data = resp.data;
                if(data.code === 200){
                    this.commentList = data.data;
                    //格式化markdown
                    for(let i=0;i<this.commentList.length;++i){
                        this.commentList[i].content = marked(this.commentList[i].content)
                        this.commentList[i].commentDate = dateFormat.dateFormat(new Date(this.commentList[i].commentDate));
                    }
                    // for(let i = 0;i<this.commentList.length;++i){
                    //     this.commentList[i].commentDate = dateFormat.dateFormat(new Date(this.commentList[i].commentDate));
                    //     //获取当前评论下的所有回复
                    //     this.queryReply(this.commentList[i].id);
                    //     Vue.set(this.commentList[i],'replyList',[]);//添加属性
                    //     console.log(this.replyList)
                    //     for(let j=0;j<this.replyList.length;++j){
                    //         let temp = JSON.parse(JSON.stringify(this.replyList[j]));
                    //         // console.log(temp === JSON.stringify(this.replyList[j]))
                    //         this.commentList[i].replyList.push(temp);
                    //     }
                    //     // console.log(this.commentList[i])
                    //     // console.log(this.replyList)
                    //     // this.replyList = [];
                    // }
                }
            })
        },
        //回复评论
        replyComment(id){
            //设置回复的评论的id
            this.comment.commentId = id;
            //设置当前题解的id
            this.comment.qid = this.qid;
            this.saveComment();
        },
        //查询当前评论的回复 id是当前评论的id index是在集合中的下标 需要使用递归来进行获取，因为回复的评论也会有回复的评论
        // queryReply(id){
        //     // console.log(this.replyList)
        //     commentApi.queryReplyCommentById(id).then(resp => {
        //         let data = resp.data;
        //         for(let i=0;i<data.data.length;++i){
        //             data.data[i].commentDate = dateFormat.dateFormat(new Date(data.data[i].commentDate));
        //             // this.commentList[index].replyList.push(data.data[i]);
        //             this.replyList.push(data.data[i])
        //             // Vue.set(this.commentList[index].replyList[i],'replyUser',data.data[i].userId);//设置回复的是哪个人的评论
        //             Vue.set(this.replyList[i],'replyUser',data.data[i].userId);//设置回复的是哪个人的评论
        //             //递归继续获取
        //             this.queryReply(this.replyList[i].id);
        //         }
        //     })
        // }
    }
}
</script>

<style scoped>
</style>