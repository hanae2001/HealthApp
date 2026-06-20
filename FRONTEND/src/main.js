import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router'
import App from './App.vue'
import './assets/main.css'

import LoginView from './views/LoginView.vue'
import DashboardView from './views/DashboardView.vue'
import AdminView from './views/AdminView.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/',          redirect: '/login' },
    { path: '/register',  redirect: '/login' },
    { path: '/login',     component: LoginView },
    { path: '/dashboard', component: DashboardView, meta: { requiresAuth: true, role: 'patient' } },
    { path: '/admin',     component: AdminView,     meta: { requiresAuth: true, role: 'admin' } },
  ],
  scrollBehavior() { return { top: 0 } }
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  const user  = JSON.parse(localStorage.getItem('user') || '{}')
  const role  = user.role || ''

  // Non authentifié → login
  if (to.meta.requiresAuth && !token) return '/login'

  // Déjà connecté → redirige selon le rôle
  if (to.path === '/login' && token && role) {
    return role === 'admin' ? '/admin' : '/dashboard'
  }

  // Mauvais rôle pour cette route (seulement si rôle connu)
  if (to.meta.role && role && to.meta.role !== role) {
    return role === 'admin' ? '/admin' : '/dashboard'
  }
})

createApp(App).use(router).mount('#app')
