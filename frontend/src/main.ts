import { createApp } from "vue";
import { createRouter, createWebHashHistory } from "vue-router";
import App from "./App.vue";
import LoginPage from "./pages/LoginPage.vue";
import WaitingPage from "./pages/WaitingPage.vue";
import GamePage from "./pages/GamePage.vue";
import ScoreboardPage from "./pages/ScoreboardPage.vue";

import "./style.css";

const routes = [
  { path: "/", component: LoginPage },
  { path: "/waiting", component: WaitingPage },
  { path: "/game", component: GamePage },
  { path: "/scoreboard", component: ScoreboardPage },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

createApp(App).use(router).mount("#app");
