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
            <el-menu-item index="/contest">比赛日程表</el-menu-item>
            <el-menu-item index="/friendLink">友链</el-menu-item>
            <el-menu-item index="/advice">推荐</el-menu-item>
            <el-menu-item index="/blog">博客</el-menu-item>
            <el-menu-item index="/practice">语言基础练习</el-menu-item>
            <el-menu-item index="/utils">工具</el-menu-item>

            <!--如果未登陆显示-->
            <el-menu-item
                style="float: right;margin-right: 150px"
                @click="changeLoginDialogStatus"
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

        <!--账号密码登陆框-->
        <el-dialog :visible.sync="isShowLogin" width="500px">
            <h3 style="text-align: center">欢迎访问本站</h3>
            <el-form label-width="80px" :label-position="labelPosition" :model="registerForm">
                <el-form-item label="用户名">
                    <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"/>
                </el-form-item>
                <div style="margin-left: 300px;">
                    <el-button size="mini" type="primary" >登陆</el-button>
                    <el-button size="mini" type="primary" @click="changeRegisterDialogStatus">注册</el-button>
                </div>
            </el-form>
            更多登陆方式:
            <a href="Javascript:void(0)">
                <img loading="lazy"
                     src="https://wiki.connect.qq.com/wp-content/uploads/2021/01/bt_blue_24X24.png"
                     width="24"
                     height="24">
            </a>
            |
            <a href="Javascript:void(0)">
                邮箱登陆
            </a>
        </el-dialog>

        <!--注册框-->
        <el-dialog :visible.sync="isShowRegister" width="500px">
            <h3 style="text-align: center">欢迎访问本站</h3>
            <el-form label-width="80px" :label-position="labelPosition" :model="registerForm">
                <el-form-item label="用户名">
                    <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"/>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="registerForm.email" type="email" placeholder="请输入邮箱"/>
                    <el-button  size="mini">获取验证码</el-button>
                    <span style=""></span>
                </el-form-item>
                <el-form-item label="验证码">
                    <el-input v-model="registerForm.vcode" placeholder="请输入验证码"/>
                </el-form-item>
                <div>
                    <a style="text-decoration: none;color: #6b6c70;display: inline-block" href="Javascript:void(0)" @click="changeLoginDialogStatus">返回登陆</a>
                    <el-button style="margin-left: 380px;display: inline-block" size="mini" type="primary" >注册</el-button>
                </div>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>

import {getToken} from '@/utils/auth'

export default {
    name: "Nav",
    data() {
        return {
            //当前被选中的路由作为被激活的导航 如果跳转到了当前路由的子路由下，就对其进行处理，让其仍然选中的是父路由
            nowRouter: '/' + this.$route.path.split('/')[1],
            //登陆的标识
            isLogin: false,
            //是否展示弹出框进行登陆
            isShowLogin: false,
            //是否展示弹出框进行注册
            isShowRegister: false,
            //注册的表单项
            registerForm:{
                username:"",//用户名
                password:"",//密码
                email:"",//邮箱
                vcode:""//验证码
            },
            //登陆注册框的对其方式
            labelPosition:'right'
        }
    },
    methods: {
        //修改 登陆弹出框的状态
        changeLoginDialogStatus(){
            this.isShowLogin = !this.isShowLogin;
            //如果注册框未关闭就关闭注册框
            if(this.isShowRegister) this.isShowRegister = !this.isShowRegister;
        },
        //修改注册弹出框的状态
        changeRegisterDialogStatus(){
            //打开注册框
            this.isShowRegister = !this.isShowRegister;
            //关闭登陆框
            this.isShowLogin = !this.isShowLogin;
        }
    }
}
</script>

<style scoped lang="css">
.el-menu-item {
    font-size: 17px;
}
</style>