<template>
    <el-row>
        <el-col :span="18" :offset="3">
            <h1 style="text-align: center">比赛日程</h1>
            <el-calendar>
                <template
                    slot="dateCell"
                    slot-scope="{date, data}">
                    <div style="width: 100%;height: 100%" @click="showContestMessage(data.day)">
                        <div :class="data.isSelected ? 'is-selected' : ''">
                            {{ data.day.split('-').slice(1).join('-') }}
                        </div>
                    </div>
                </template>
            </el-calendar>
            <!--弹出框 显示当天比赛-->
            <el-dialog top="25vh" title="日期" :visible.sync="isShowDialog">
                <el-table :data="contestList">
                    <el-table-column property="contestName" label="比赛名称"></el-table-column>
                    <el-table-column property="platform" label="平台"></el-table-column>
                    <el-table-column label="地址" min-width="250">
                        <template v-slot="scope">
                            <a href="">{{scope.row.link}}</a>
                        </template>
                    </el-table-column>
                </el-table>
            </el-dialog>
        </el-col>

    </el-row>
</template>

<script>
import contestApi from "@/api/contest";

export default {
    name: "contest",
    data(){
        return{
            isShowDialog:false,
            contestList:[],
            flag:false
        }
    },
    methods:{
        //给弹出框渲染比赛数据
        showContestMessage(date){
            this.isShowDialog = !this.isShowDialog
            contestApi.queryContestByDate(date).then(resp => {
                let data = resp.data;
                if(data.code === 200){
                    console.log(data)
                    this.contestList = data.data;
                }else{
                    this.$message.warning({
                        message:"获取比赛失败，请刷新重试",
                        duration:2000
                    })
                }
            }).catch(error => {
                this.$message.error({
                    message:"获取比赛失败，请检查网络",
                    duration:2000
                })
            })
        }
    }
}
</script>

<style scoped>

</style>