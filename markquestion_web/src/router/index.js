import Vue from "vue"
import VueRouter from "vue-router";
import index from "@/views/index";
import author from "@/views/author/author";
import questionDetail from "@/views/question/questionDetail";
import description from "@/views/author/description/description";
import wrongPracticeList from "@/views/author/wrong_practice/list";
import solutionList from "@/views/author/solution/list";
import messageBoard from "@/views/author/message_board/list"
import contest from "@/views/contest/contest";
import friendLinkList from "@/views/friend_link/friendLinkList";
import blogIndex from "@/views/blog/index"
import detail from "@/views/author/wrong_practice/detail";
import adviceList from "@/views/advice/adviceList";
import practiceList from "@/views/practice/practiceList";
import utilsList from "@/views/utils/utilsList";
import content from "@/views/blog/content";


Vue.use(VueRouter)

let vueRouter =  new VueRouter({
    mode: "history",
    routes: [
        //根路径直接跳转到首页
        {
            path:"/",
            redirect:"/index",
        },
        //首页
        {
            path:"/index",
            component:index
        },
        //博主信息页
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
                    component:wrongPracticeList,
                    children:[
                        {
                            path:'detail',
                            component:detail
                        }
                    ]
                },
                {
                    path:'messageBoard',
                    component:messageBoard
                }
            ]
        },
        //题解详情页面
        {
            path:"/index/questionDetail/:id",
            component:questionDetail
        },
        //比赛日程表页面
        {
            path:"/contest",
            component:contest
        },
        //友链页面
        {
            path:"/friendLink",
            component:friendLinkList
        },
        //博客页面
        {
            path:"/blog",
            component:blogIndex
        },
        //推荐页面
        {
            path:"/advice",
            component:adviceList
        },
        //练习页面
        {
            path:"/practice",
            component:practiceList
        },
        //工具页面
        {
            path:"/utils",
            component:utilsList
        },
        //博客展示页面
        {
            path:"/content",
            component:content
        }
    ]
})

export default vueRouter;