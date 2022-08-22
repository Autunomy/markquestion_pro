import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import current from "element-ui/packages/table/src/store/current";

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

let permissionList = []

//检查路由权限
function checkChildrenRoutePermission(currentRoute){
  if(currentRoute.meta && currentRoute.meta.permission && permissionList.includes(currentRoute.meta.permission)){
    currentRoute.hidden = false;
  }else{
    currentRoute.hidden = true;
  }

  if(currentRoute.children){
    currentRoute.children.forEach(route => {
      checkChildrenRoutePermission(route)
    })
  }
}

// router.beforeEach(async(to, from, next) => {
//   // start progress bar
//   NProgress.start()
//
//   // set page title
//   document.title = getPageTitle(to.meta.title)
//
//   // determine whether the user has logged in
//   const hasToken = getToken()
//
//   if (hasToken) {
//
//     let routes = router.options.routes;
//     permissionList = window.sessionStorage.getItem("permissionList").split(",");
//     if(permissionList){
//       routes.forEach(route => {
//         checkChildrenRoutePermission(route);
//       })
//     }
//
//     if (to.path === '/login') {
//       // if is logged in, redirect to the home page
//       next({ path: '/' })
//       NProgress.done()
//     } else {
//       const hasGetUserInfo = store.getters.name
//       if (hasGetUserInfo) {
//         next()
//       } else {
//
//         next()
//         NProgress.done()
//       }
//     }
//   } else {
//     /* has no token*/
//
//     if (whiteList.indexOf(to.path) !== -1) {
//       // in the free login whitelist, go directly
//       next()
//     } else {
//       // other pages that do not have permission to access are redirected to the login page.
//       next(`/login?redirect=${to.path}`)
//       NProgress.done()
//     }
//   }
// })

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
