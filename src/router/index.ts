import { createRouter, createWebHistory } from 'vue-router'

import type { RouteRecordRaw } from 'vue-router'

const Layout = () => import('@/layout/index.vue')

const routes: RouteRecordRaw[] = [
  {
    path: '/sign-in',
    name: 'signIn',
    component: () => import('@/views/sign-in/index.vue'),
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '数据概览', icon: 'ph--chart-bar' },
      },
      {
        path: 'profile',
        name: 'profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'ph--user' },
      },
    ],
  },
  {
    path: '/student',
    component: Layout,
    redirect: '/student/jobs',
    children: [
      {
        path: 'jobs',
        name: 'studentJobs',
        component: () => import('@/views/student/JobList.vue'),
        meta: { title: '兼职信息', icon: 'ph--briefcase' },
      },
      {
        path: 'my-applies',
        name: 'studentApplies',
        component: () => import('@/views/student/MyApplies.vue'),
        meta: { title: '我的申请', icon: 'ph--file-text' },
      },
    ],
  },
  {
    path: '/admin',
    component: Layout,
    redirect: '/admin/jobs',
    children: [
      {
        path: 'jobs',
        name: 'adminJobs',
        component: () => import('@/views/admin/JobManage.vue'),
        meta: { title: '兼职管理', icon: 'ph--briefcase' },
      },
      {
        path: 'applies',
        name: 'adminApplies',
        component: () => import('@/views/admin/ApplyManage.vue'),
        meta: { title: '申请管理', icon: 'ph--clipboard-text' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'errorPage',
    component: () => import('@/views/error-page/index.vue'),
  },
]

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  strict: true,
})

export default router
