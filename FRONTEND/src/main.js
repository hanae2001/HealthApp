import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router'
import App from './App.vue'
import './assets/main.css'

import RegisterView from './views/RegisterView.vue'
import LoginView from './views/LoginView.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/',          redirect: '/register' },
    { path: '/register',  component: RegisterView },
    { path: '/login',     component: LoginView }
  ],
  scrollBehavior() {
    return { top: 0 }
  }
})

createApp(App).use(router).mount('#app')
