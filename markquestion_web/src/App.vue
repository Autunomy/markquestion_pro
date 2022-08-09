<template>
    <div id="app">
        <el-container>
            <el-header>
                <Nav/>
            </el-header>
            <el-main>
                <router-view/>
            </el-main>
            <el-footer>
                <Footer></Footer>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
import Nav from "@/components/Nav";
import Footer from "@/components/Footer";

export default {
    name: 'App',
    components: {
        Footer,
        Nav
    }
}
</script>
<style>
.el-header{
    padding: 0 0;
}
.el-main{
    padding: 0 0;
}
.el-footer{
    padding: 0 0;
}
.el-container{
    min-height: 100vh;
}
</style>
