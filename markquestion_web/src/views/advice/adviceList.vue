<template>
    <div>
        <el-row style="margin-top: 10px">
            <el-col :span="20" :offset="2">
                <el-collapse accordion>
                    <!--列表-->
                    <el-collapse-item v-for="advice in adviceList">
                        <template slot="title">
                            <el-avatar shape="square" :size="'small'" :src="advice.adviceImg"></el-avatar>
                            <div style="font-size: 18px">
                                {{advice.adviceName}}
                                <el-tag type="mini" style="margin-left: 5px">{{advice.adviceTag}}</el-tag>
                                <div style="margin-left: 10px;display: inline-block;font-size: 15px">
                                    浏览量:{{advice.adviceWatch}}
                                </div>
                                <div style="margin-left: 10px;display: inline-block;font-size: 15px">
                                    描述:{{advice.adviceDescribe}}
                                </div>
                            </div>
                        </template>
                        <div style="font-size: 18px">
                            <div style="border: 1px solid #c4ccc4;width: 100%;height: 100%;padding: 5px;border-radius: 5px">
                                <div v-highlight>
                                    <div v-html="advice.adviceContent"></div>
                                </div>
                            </div>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </el-col>
        </el-row>

    </div>
</template>

<script>
import adviceApi from "@/api/advice";
import {marked} from "marked";

export default {
    name: "adviceList",
    data(){
        return{
            //推荐列表
            adviceList:[]
        }
    },
    mounted() {
        this.queryAdviceList();
    },
    methods:{
        //获取全部的推荐
        queryAdviceList(){
            adviceApi.queryAdviceList().then(resp => {
                this.adviceList = resp.data.data;
                for(let i = 0;i<this.adviceList.length;++i){
                    //格式化markdown
                    this.adviceList[i].adviceContent = marked(this.adviceList[i].adviceContent)
                    this.adviceList[i].adviceImg = "http://180.76.97.59:8001"+this.adviceList[i].adviceImg;
                }
            })
        }
    }
}
</script>

<style scoped>

</style>