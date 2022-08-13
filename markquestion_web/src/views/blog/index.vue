<template>
    <el-row style="margin-bottom: 20px">
        <div style="margin-top: 20px">
            <!--分类专栏-->
            <el-col :span="4" :offset="3">
                <!--头像框-->
                <el-card>
                    <hr style="margin: 0 0">
                    <div>
                        <a @click="changeBlogClass" href="Javascript:void(0)" style="color: black">
                            <el-row>
                                <el-col :span="2">
                                    <h4>javase</h4>
                                </el-col>
                                <el-col :span="6" :offset="16">
                                    <h5 style="margin-top: 12px">1000篇</h5>
                                </el-col>
                            </el-row>
                        </a>
                        <hr style="margin: 0 0">
                    </div>
                </el-card>
            </el-col>
            <el-col :span="14">
                <div>
                    <a href="/content?id=1" style="text-decoration: none;display: inline-block;margin-bottom: 5px">
                        <el-card style="margin-left: 6px">
                            <h3 style="margin: 0 0">使用Druid连接池创建DataSource(数据源)</h3>
                            <p style="margin-top: 6px;color: #555b6b">使用properties配置文件的方式进行配置,在项目根目录下创建一个resources文件并标记为资源目录,创建一个db.properties的文件并写入以下配置...</p>
                            <i class="el-icon-time">2022-01-01</i>
                            -
                            <span>阅读量:20</span>
                            -
                            <span>评论:0</span>
                        </el-card>
                    </a>
                </div>
                <div>
                    <a href="Javascript:void(0)" style="text-decoration: none;display: inline-block;margin-bottom: 5px">
                        <el-card style="margin-left: 6px">
                            <h3 style="margin: 0 0">使用Druid连接池创建DataSource(数据源)</h3>
                            <p style="margin-top: 6px;color: #555b6b">使用properties配置文件的方式进行配置,在项目根目录下创建一个resources文件并标记为资源目录,创建一个db.properties的文件并写入以下配置...</p>
                            <i class="el-icon-time">2022-01-01</i>
                            -
                            <span>阅读量:20</span>
                            -
                            <span>评论:0</span>
                        </el-card>
                    </a>
                </div>
                <div>
                    <a href="Javascript:void(0)" style="text-decoration: none;display: inline-block;margin-bottom: 5px">
                        <el-card style="margin-left: 6px">
                            <h3 style="margin: 0 0">使用Druid连接池创建DataSource(数据源)</h3>
                            <p style="margin-top: 6px;color: #555b6b">使用properties配置文件的方式进行配置,在项目根目录下创建一个resources文件并标记为资源目录,创建一个db.properties的文件并写入以下配置...</p>
                            <i class="el-icon-time">2022-01-01</i>
                            -
                            <span>阅读量:20</span>
                            -
                            <span>评论:0</span>
                        </el-card>
                    </a>
                </div>
            </el-col>
        </div>
    </el-row>
</template>

<script>
import blogApi from "@/api/blog";

export default {
    name: "index",
    data() {
        return{

        }
    },
    methods: {
        //点击分类之后可以切换分类
        changeBlogClass(){

        }
    }
}
</script>

<style scoped>

</style>