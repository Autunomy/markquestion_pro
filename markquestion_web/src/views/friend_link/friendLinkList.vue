<template>
<div>
    <el-row style="margin-top: 10px">
        <el-col :span="20" :offset="2">
            <el-collapse accordion>
                <!--列表-->
                <el-collapse-item v-for="friendLink in friendLinkList">
                    <template slot="title">
                       <div style="font-size: 18px">
                           {{friendLink.linkName}}
                           <el-tag type="mini" style="margin-left: 5px">{{friendLink.tag}}</el-tag>
                           <div style="margin-left: 10px;display: inline-block;font-size: 15px">
                               简介:{{ friendLink.introduce }}
                           </div>
                       </div>
                    </template>
                    <div style="font-size: 18px">
                        链接地址: <a target="_blank" :href="friendLink.link">点我跳转</a> <br>
                        <div style="border: 1px solid #c4ccc4;width: 100%;height: 100%;padding: 5px;border-radius: 5px">
                            <div v-highlight>
                                <div v-html="friendLink.description"></div>
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
import friendLinkApi from "@/api/friendLink";
import {marked} from "marked";

export default {
    name: "friendLinkList",
    data(){
        return{
            //友链列表
            friendLinkList:[]
        }
    },
    mounted() {
        this.queryFriendLinkList();
    },
    methods:{
        //获取全部的友链
        queryFriendLinkList(){
            friendLinkApi.queryFriendLinkList().then(resp => {
                this.friendLinkList = resp.data.data;
                for(let i = 0;i<this.friendLinkList.length;++i){
                    //格式化markdown
                    this.friendLinkList[i].description = marked(this.friendLinkList[i].description)
                }
            })
        }
    }
}
</script>

<style scoped>

</style>