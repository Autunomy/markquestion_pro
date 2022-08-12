<template>
<div>
    <el-row style="margin-bottom: 20px">
        <div style="margin-top: 20px">
            <el-col :span="4" :offset="3">
                <!--头像框-->
                <el-card>
                    <div style="padding: 14px;">
                        <el-image
                            style="width: 100%; height: 200px"
                            :src="'http://www.autunomy.top/images/head/head.jpg'"
                            :fit="'cover'"></el-image>
                        <h2 style="text-align: center">Autunomy</h2>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="14">
                <el-card style="margin-left: 6px">
                    <el-tabs v-model="activeTab" @tab-click="handleClick">
                        <el-tab-pane label="博主介绍" name="/author/description" route="/author/description"></el-tab-pane>
                        <el-tab-pane label="题解列表" name="/author/solutionList"></el-tab-pane>
                        <el-tab-pane label="错题集" name="/author/wrongPracticeList"></el-tab-pane>
                        <el-tab-pane label="留言板" name="/author/messageBoard"></el-tab-pane>
                        <!--路由显示区域-->
                        <router-view/>
                    </el-tabs>
                </el-card>
            </el-col>
        </div>
    </el-row>
</div>
</template>

<script>
export default {
    name: "author",
    data(){
        return{
            activeTab:'/author/description'//当前选中的标签
        }
    },
    mounted() {
        //一旦用户访问当前页面，路由就跳转到博主介绍页面
        this.$router.push('/author/description');
    },
    methods:{
        //点击标签之后进行路由跳转
        handleClick(tab){
            this.$router.push(tab.name);
        }
    }
}
</script>

<style scoped>

</style>