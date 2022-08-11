<template>
    <div>
        <el-table
            :data="tableData"
            style="width: 100%">
            <el-table-column
                prop="questionName"
                label="题解标题"
                min-width="280">
                <template v-slot="scope">
                    <a :href="'/index/questionDetail/'+scope.row.id">{{scope.row.questionName}}</a>
                </template>
            </el-table-column>
            <el-table-column
                prop="tag"
                label="语言"
                min-width="180">
                <template v-slot="scope">
                    <el-tag :type="'primary'" disable-transitions>{{scope.row.tag}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                prop="level"
                label="难度">
                <template v-slot="scope">
                    <el-tag :type="scope.row.level === '简单' ? 'success' : (scope.level === '中等' ? 'warning' : 'danger')"
                            effect="dark"
                            disable-transitions>{{scope.row.level}}</el-tag>
                </template>
            </el-table-column>
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
    </div>
</template>

<script>
import questionApi from "@/api/question";
import qs from "qs";
export default {
    name: "list",
    data(){
        return{
            tableData:[],
            pageInfo:{
                author:'Autunomy',
                currentPage:1,
                pageSize:8,
                total:0
            }
        }
    },
    mounted() {
        //挂载之后就渲染数据
        this.queryQuestionPageByAuthor();
    },
    methods:{
        queryQuestionPageByAuthor(){
            //查询数据
            questionApi.queryQuestionPageByAuthor(qs.stringify(this.pageInfo)).then(resp => {
                let data = resp.data;
                if(data.code === 200){
                    //渲染数据
                    this.tableData = data.data;
                    console.log(this.tableData)
                    this.pageInfo.currentPage = data.pageInfo.currentPage;
                    this.pageInfo.pageSize = data.pageInfo.pageSize;
                    this.pageInfo.total = data.pageInfo.total;
                }
            })
        },
        //页码改变
        changeCurrentPage(currentPage){
            //设置当前页码
            this.pageInfo.currentPage = currentPage;
            //向服务器查询数据
            this.queryQuestionPageByAuthor();
        }
    }
}
</script>

<style scoped>

</style>