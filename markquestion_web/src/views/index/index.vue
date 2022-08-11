<template>
    <el-row style="margin-top: 20px">
        <el-col :span="20" :offset="2">
            <!--搜索框-->
            <div style="float: right;">
                <el-form onsubmit="return false;" :inline="true" class="demo-form">
                    <el-form-item label="">
                        <el-input maxlength="20"
                                  style="width: 400px"
                                  v-model="search"
                                  @keyup.native.enter="searchQuestion"
                                  placeholder="根据编号，题目名称，题目来源，作者搜索"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="searchQuestion">搜索</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <!--题解表格-->
            <el-table
                :data="questionTable"
                style="width: 100%;font-size: 17px;">
                <el-table-column prop="id" label="编号"></el-table-column>
                <el-table-column prop="questionName" label="题目名称" min-width="150px">
                    <template v-slot="scope">
                        <router-link style="text-decoration: none;color:cornflowerblue"
                                     :to="'/index/questionDetail/'+scope.row.id">{{scope.row.questionName}}</router-link>
                    </template>
                </el-table-column>
                <el-table-column prop="questionFrom" label="题目来源"></el-table-column>
                <el-table-column prop="author" label="作者"></el-table-column>
                <el-table-column prop="tag" label="语言">
                    <template v-slot="scope">
                        <el-tag :type="'primary'" disable-transitions>{{scope.row.tag}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="tag" label="难度">
                    <template v-slot="scope">
                        <el-tag :type="scope.row.level === '简单' ? 'success' : (scope.level === '中等' ? 'warning' : 'danger')"
                                effect="dark"
                                disable-transitions>{{scope.row.level}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="watch" label="浏览量"></el-table-column>
            </el-table>
            <!--分页组件-->
            <el-pagination
                background
                layout="prev, pager, next"
                :total="pageInfo.total"
                :page-size="pageInfo.pageSize"
                :pager-count="5"
                :current-page.sync="pageInfo.currentPage"
                style="margin: 20px auto;text-align: center"
                @current-change="changeCurrentPage">
            </el-pagination>
        </el-col>
    </el-row>
</template>

<script>
import questionApi from "@/api/question";
import qs from "qs"

export default {
    name: "index",
    data(){
        return{
            //渲染到页面的题解列表
            questionTable:[],
            //分页信息
            pageInfo:{
                currentPage:1,//当前页码
                pageSize:15,//页面数据的数量
                total:100//数据总条数
            },
            //做查询的表单数据
            search:''
        }
    },
    mounted() {
        //当Vue实例化之后就渲染数据
        this.queryQuestionPage();
    },
    methods:{
        //将分页信息携带过做分页查询
        queryQuestionPage(){
            questionApi.queryQuestionPage(qs.stringify(this.pageInfo)).then(resp => {
                if(resp.data.code === 200){//状态码200表示成功
                    //渲染当前页面数据
                    this.questionTable = resp.data.data;
                    //设置分页信息 方式当前分页信息与数据库信息不匹配
                    this.pageInfo = resp.data.pageInfo;
                }else{//获取失败
                    this.$message.warning({
                        message:"获取题解列表失败,请刷新重试",
                        duration:2000 //显示时间2s
                    })
                }
            }).catch(error => {
                //网络请求没有发送过去
                this.$message.error({
                    message:"获取题解列表失败，请检查网络",
                    duration:2000
                })
            })
        },
        //搜索题目
        searchQuestion(){
            //搜索条件为空表示搜索全部信息
            if(this.search !== "" && this.search !== null){
                //搜索条件
                let data = "search=" + this.search + "&" + qs.stringify(this.pageInfo);
                questionApi.searchQuestion(data).then(resp => {
                    let data = resp.data;
                    if(data.code === 200){
                        this.pageInfo.currentPage = 1;
                        this.questionTable = data.data;
                        this.pageInfo = data.pageInfo;
                    }
                })
            }else{
                this.queryQuestionPage();
            }
        },
        //页码改变时的回调函数 参数是改变后的页码
        changeCurrentPage(currentPage){
            //设置当前页码
            this.pageInfo.currentPage = currentPage;
            //向服务器查询数据
            this.queryQuestionPage();
        }
    }
}
</script>

<style scoped>

</style>