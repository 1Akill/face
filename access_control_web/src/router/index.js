import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/face',
    name: 'face',
    component: () => import('@/views/Face.vue')
  },
  {
    path: '/',
    name: 'adminLogin',
    component: () => import('@/views/AdminLogin.vue')
  },
  {
    path: '/main',
    name: 'main',
    component:  () => import('@/views/MainMenu.vue'),
    children:[

      {
        path: '/register',
        name: 'register',
        component:  () => import('@/views/Register.vue')
      },
      {
        path: '/userList',
        name: 'userList',
        component:  () => import('@/views/UserList.vue')
      },
      {
        path: '/accessRecord',
        name: 'accessRecord',
        component:  () => import('@/views/AccessRecord.vue')
      },
      {
        path: '/visitorAdd',
        name: 'visitorAdd',
        component:  () => import('@/views/VisitorAdd.vue')
      },
      {
        path: '/visitorList',
        name: 'visitorList',
        component:  () => import('@/views/VisitorList.vue')
      },

    ]
  },
]



const router = new VueRouter({
  routes
})


router.beforeEach((to, from, next) => {
  // 1. 判断是不是登录页面
  // 是登录页面
  console.log("路由",to)
  if(to.path === '/' || to.path === '/face') {
    next()
  } else {
    // 不是登录页面
    // 2. 判断 是否登录过
    let token = localStorage.getItem('Auth-token')
    console.log("token",token)
    token ? next() : next('/')
  }
})
export default router
