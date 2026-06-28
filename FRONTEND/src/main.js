import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router'
import App from './App.vue'
import './assets/main.css'

import LoginView          from './views/LoginView.vue'
import DashboardView      from './views/DashboardView.vue'
import AdminView          from './views/AdminView.vue'
import MedecinView        from './views/MedecinView.vue'
import ReceptionnisteView from './views/ReceptionnisteView.vue'

const ROLE_HOME = {
  admin:          '/admin',
  patient:        '/dashboard',
  medecin:        '/medecin',
  receptionniste: '/receptionniste',
}

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/',               redirect: '/login' },
    { path: '/register',       redirect: '/login' },
    { path: '/login',          component: LoginView },
    { path: '/dashboard',      component: DashboardView,      meta: { requiresAuth: true, role: 'patient' } },
    { path: '/admin',          component: AdminView,           meta: { requiresAuth: true, role: 'admin' } },
    { path: '/medecin',        component: MedecinView,         meta: { requiresAuth: true, role: 'medecin' } },
    { path: '/receptionniste', component: ReceptionnisteView,  meta: { requiresAuth: true, role: 'receptionniste' } },
  ],
  scrollBehavior() { return { top: 0 } }
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  const user  = JSON.parse(localStorage.getItem('user') || '{}')
  const role  = user.role || ''

  if (to.meta.requiresAuth && !token) return '/login'

  if (to.path === '/login' && token && role) {
    return ROLE_HOME[role] || '/dashboard'
  }

  if (to.meta.role && role && to.meta.role !== role) {
    return ROLE_HOME[role] || '/dashboard'
  }
})

createApp(App).use(router).mount('#app')
