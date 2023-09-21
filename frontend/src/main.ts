import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router'
import App from './App.vue'
import GamePage from './pages/GamePage.vue'
import WaitingPage from './pages/WaitingPage.vue'
import './style.css'

const routes = [
  { path: '/', component: GamePage },
  { path: '/waiting', component: WaitingPage },
  { path: '/game', component: GamePage }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})


createApp(App)
  .use(router)
  .mount('#app');
