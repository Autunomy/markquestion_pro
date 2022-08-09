import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import axios from "axios";
import router from "@/router";
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import Highlight from './utils/highlight'

//引入全局样式
import './assets/css/removeOld.css'//去除浏览器的默认padding和margin

Vue.use(ElementUI)
Vue.use(mavonEditor) //markdown编辑器支持图片上传
Vue.use(Highlight) //代码高亮

Vue.prototype.$axios = axios
Vue.config.productionTip = false


new Vue({
  el: '#app',
  router,
  axios,
  render: h => h(App)
});