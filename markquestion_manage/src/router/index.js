import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true,
    meta: {
      title: "用户登陆"
    }
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: {title: '首页日志', icon: 'dashboard'}
    }]
  },

  {
    path: '/question',
    component: Layout,
    redirect: '/question/list',
    name: 'question',
    meta: {title: '题解管理', icon: 'el-icon-film'},
    children: [
      {
        path: 'list',
        name: 'questionList',
        component: () => import('@/views/question/list'),
        meta: {title: '题解列表', icon: 'el-icon-document'}
      },
      {
        path: 'addQuestion',
        name: 'addQuestion',
        component: () => import('@/views/question/addQuestion'),
        meta: {title: '添加题解', icon: 'el-icon-news'}
      },
      {
        path: 'updateQuestion',
        name: 'updateQuestion',
        component: () => import('@/views/question/updateQuestion'),
        meta: {title: '修改题解', icon: 'el-icon-news'}
      }
    ]
  },

  {
    path: '/author',
    component: Layout,
    redirect: '/author/messageBoard',
    name: 'author',
    meta: {title: '博主信息管理', icon: 'el-icon-film'},
    children: [
      {
        path: 'messageBoard',
        name: 'messageBoard',
        component: () => import('@/views/author/messageBoard'),
        meta: {title: '留言列表', icon: 'el-icon-document'}
      },
      {
        path: 'authorMessage',
        name: 'authorMessage',
        component: () => import('@/views/author/authorMessage'),
        meta: {title: '博主信息管理', icon: 'el-icon-document'}
      },
      {
        path: 'wrongAnswer',
        name: 'wrongAnswer',
        component: () => import('@/views/author/wrongAnswer'),
        meta: {title: '错题管理', icon: 'el-icon-document'}
      }
    ]
  },
  {
    path: '/contest',
    component: Layout,
    redirect: '/contest/contestList',
    name: 'contest',
    meta: {title: '比赛日程管理', icon: 'el-icon-film'},
    children: [
      {
        path: 'contestList',
        name: 'contestList',
        component: () => import('@/views/contest/contestList'),
        meta: {title: '比赛列表', icon: 'el-icon-document'}
      },
      {
        path: 'addContest',
        name: 'addContest',
        component: () => import('@/views/contest/addContest'),
        meta: {title: '添加比赛', icon: 'el-icon-document'}
      }
    ]
  },
  {
    path: '/friendLink',
    component: Layout,
    redirect: '/friendLink/list',
    name: 'contest',
    meta: {title: '友链管理', icon: 'el-icon-film'},
    children: [
      {
        path: 'list',
        name: 'list',
        component: () => import('@/views/friendLink/list'),
        meta: {title: '友链列表', icon: 'el-icon-document'}
      },
      {
        path: 'add',
        name: 'add',
        component: () => import('@/views/friendLink/add'),
        meta: {title: '添加友链', icon: 'el-icon-document'}
      }
    ]
  },
  {
    path: '/advice',
    component: Layout,
    redirect: '/friendLink/list',
    name: 'advice',
    meta: {title: '推荐管理', icon: 'el-icon-film'},
    children: [
      {
        path: 'list',
        name: 'list',
        component: () => import('@/views/advice/list'),
        meta: {title: '推荐列表', icon: 'el-icon-document'}
      },
      {
        path: 'add',
        name: 'add',
        component: () => import('@/views/advice/add'),
        meta: {title: '添加推荐', icon: 'el-icon-document'}
      }
    ]
  },

  {
    path: '/blog',
    component: Layout,
    redirect: '/blog/list',
    name: 'blog',
    meta: {title: '博客管理', icon: 'el-icon-film'},
    children: [
      {
        path: 'list',
        name: 'list',
        component: () => import('@/views/blog/list'),
        meta: {title: '博客列表', icon: 'el-icon-document'}
      },
      {
        path: 'add',
        name: 'add',
        component: () => import('@/views/blog/add'),
        meta: {title: '博客推荐', icon: 'el-icon-document'}
      }
    ]
  },
  {
    path: '/practice',
    component: Layout,
    redirect: '/practice/list',
    name: 'practice',
    meta: {title: '练习管理', icon: 'el-icon-film'},
    children: [
      {
        path: 'list',
        name: 'list',
        component: () => import('@/views/practice/list'),
        meta: {title: '练习列表', icon: 'el-icon-document'}
      },
      {
        path: 'add',
        name: 'add',
        component: () => import('@/views/practice/add'),
        meta: {title: '练习推荐', icon: 'el-icon-document'}
      }
    ]
  },
  // 404 page must be placed at the end !!!
  {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
