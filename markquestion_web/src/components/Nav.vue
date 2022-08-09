<template>
    <div>
        <!--导航栏-->
        <el-menu :default-active="nowRouter"
                 class="el-menu-demo"
                 mode="horizontal"
                 background-color="#545c64"
                 text-color="#fff"
                 active-text-color="#ffd04b"
                 :router="true">
            <el-menu-item index="/index" style="margin-left: 160px;">首页</el-menu-item>
            <el-menu-item index="/author">博主信息</el-menu-item>
            <el-menu-item index="3">比赛日程表</el-menu-item>
            <el-menu-item index="4">友链</el-menu-item>
            <el-menu-item index="5">推荐</el-menu-item>
            <el-menu-item index="6">语言基础练习</el-menu-item>

            <!--如果未登陆显示-->
            <el-menu-item
                style="float: right;margin-right: 150px"
                @click="changeDialogStatus"
                v-if="!isLogin">
                登陆 | 注册
            </el-menu-item>

            <!--如果登陆了显示-->
            <el-submenu
                        style="float: right;margin-right: 150px"
                        v-if="isLogin">
                <template slot="title">
                    <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                </template>
                <el-menu-item style="text-align: center">个人信息</el-menu-item>
                <el-menu-item style="text-align: center">退出</el-menu-item>
            </el-submenu>
        </el-menu>

        <!--注册框-->
        <el-dialog :visible.sync="isShow">
            <el-form :model="registerForm">
                <h1>注册和登陆实现</h1>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" >注册</el-button>
                <el-button @click="changeDialogStatus">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

import {getToken} from '@/utils/auth'

export default {
    name: "Nav",
    data() {
        return {
            //当前被选中的路由 作为被激活的导航 如果跳转到了当前路由的子路由下，就对其进行处理，让其仍然选中的是父路由
            nowRouter: '/' + this.$route.path.split('/')[1],
            //登陆的标识
            isLogin: false,
            //是否展示弹出框进行注册或者登陆
            isShow: false,
            //注册的表单项
            registerForm:{
                username:"",//用户名
                password:"",//密码
                email:"",//邮箱
                vcode:""//验证码
            }
        }
    },
    methods: {
        //判断用户是否登陆
        judgeIsLogin() {
            if (getToken()) {
                this.isLogin = true
            }else{
                this.isLogin = false;
            }
        },
        //修改 登陆注册弹出框的状态
        changeDialogStatus(){
            this.isShow = !this.isShow;
        }
    }
}
</script>

<style scoped lang="css">
.el-menu-item {
    font-size: 17px;
}
</style>