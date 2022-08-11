<template>
    <div>
        <div style="margin-left: 5px;">
            <b style="font-size: 20px">写下你的留言吧(支持markdown语法)</b>
            <el-button @click="clearMessage"
                       style="float:right;margin-right: 5px"
                       size="mini"
                       type="primary">清空</el-button>
            <el-button @click="saveMessage"
                       style="float:right;margin-right: 5px"
                       size="mini"
                       type="primary">留言</el-button>
        </div>
        <!--markdown编辑器-->
        <div style="border: 3px solid #c4ccc4;border-radius: 10px;box-shadow: 0 0 5px #c4ccc4;margin: 5px">
            <mavon-editor
                @change="change"
                placeholder="有什么想对博主说的都可以在这里写下"
                :toolbarsFlag="false"
                style="min-height: 100px;"
                :subfield="false"
                v-model="this.message.message"></mavon-editor>
        </div>

        <!--分页-->
        <div>
            <el-pagination
                background
                layout="total,prev, pager, next"
                :total="pageInfo.total"
                :page-size="pageInfo.pageSize"
                :pager-count="5"
                :current-page.sync="pageInfo.currentPage"
                style="margin: 20px auto;text-align: center"
                @current-change="changeCurrentPage">
            </el-pagination>
        </div>

        <!--留言板区域-->
        <div v-for="(msg,index) in this.messageBoard">
            <el-card class="box-card">
                <div class="text item">
                    <!--第一条留言-->
                    <div>
                        <div style="font-size: 18px">
                            网友留言
                            <div style="float: right">{{msg.createDate}}</div>
                        </div>
                        <br>
                        <div v-highlight>
                            <div v-html="msg.message"></div>
                        </div>
                    </div>
                </div>
            </el-card>
            <el-divider v-if="index !== 4"></el-divider>
        </div>
    </div>
</template>

<script>
import messageBoardApi from "@/api/messageBoard";
import qs from "qs";
import {marked} from "marked"
import dateFormat from "@/utils/dateFormat";

export default {
    name: "list",
    data() {
        return {
            //分页信息
            pageInfo: {
                currentPage: 1,
                pageSize: 5,
                total: 0
            },
            //编辑框中的值
            message: {
                message:''
            },
            //留言信息
            messageBoard:[]
        }
    },
    mounted() {
        //分页获取留言信息
        this.queryMessageBoard();
    },
    methods:{
        //页码改变时的回调函数
        changeCurrentPage(currentPage){
            this.pageInfo.currentPage = currentPage;
            this.queryMessageBoard();
        },
        //编辑区的内容发生改变的回调函数 value是输入框中的内容 render是解析后的内容
        change(value,render){
            this.message.message = value;
        },
        //提交留言信息
        saveMessage(){
            if(this.message.message !== "" && this.message.message !== null){
                messageBoardApi.saveMessage(qs.stringify(this.message)).then(resp => {
                    if(resp.data.code === 200){
                        //添加完成之后还需要在进行一次留言获取
                        this.queryMessageBoard();
                        this.$message.success({
                            message:"留言成功",
                            duration:1500
                        })
                    }else{
                        this.$message.success({
                            message:"留言失败",
                            duration:1500
                        })
                    }
                })
            }else{
                this.$message.warning({
                    message:"留言不能为空",
                    duration:1500
                })
            }

        },
        queryMessageBoard(){
            messageBoardApi.queryMessageBoard(qs.stringify(this.pageInfo)).then(resp => {
                this.messageBoard = resp.data.data;
                for(let i=0;i<this.messageBoard.length;++i){
                    //markdown转html
                    this.messageBoard[i].message = marked(this.messageBoard[i].message)
                    //日期格式化
                    this.messageBoard[i].createDate = dateFormat.dateFormat(new Date(this.messageBoard[i].createDate));
                }
                //重设分页信息
                this.pageInfo = resp.data.pageInfo;
            })
        },
        //清空编辑框内容
        clearMessage(){
            this.message.message = ""
        }
    }
}
</script>

<style scoped>

</style>