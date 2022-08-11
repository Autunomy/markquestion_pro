<template>
    <div>
        <!--错题列表显示-->
        <el-table
            :data="tableData"
            style="width: 100%">
            <el-table-column
                prop="questionName"
                label="题号"
                min-width="280">
                <template v-slot="scope">
                    <a :href="'/index/questionDetail/'+scope.row.id">{{scope.row.questionName}}</a>
                </template>
            </el-table-column>
            <el-table-column
                prop="tag"
                label=""
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
export default {
    name: "list",
    data(){
        return {
            tableData:[]
        }
    }
}
</script>

<style scoped>

</style>