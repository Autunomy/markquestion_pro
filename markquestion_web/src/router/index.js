import Vue from "vue"
import VueRouter from "vue-router";
import index from "@/views/index";
import author from "@/views/author/author";
import questionDetail from "@/views/question/questionDetail";
import description from "@/views/author/description/description";
import wrongPracticeList from "@/views/author/wrong_practice/list";
import solutionList from "@/views/author/solution/list";


Vue.use(VueRouter)

let vueRouter =  new VueRouter({
    mode: "history",
    routes: [
        {
            path:"/",
            redirect:"/index",
        },
        {
            path:"/index",
            component:index
        },
        {
            path:"/author",
            component:author,
            children:[
                {
                    path:'description',
                    component:description
                },
                {
                    path:'solutionList',
                    component:solutionList
                },
                {
                    path:'wrongPracticeList',
                    component:wrongPracticeList
                }
            ]
        },
        {
            path:"/index/questionDetail/:id",
            component:questionDetail
        }
    ]
})

export default vueRouter;