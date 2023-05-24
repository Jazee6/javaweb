import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css';
import {createRouter, createWebHistory} from "vue-router";
import {createPinia} from "pinia";

const pinia = createPinia()

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/login',
            component: () => import('./components/login.vue')
        },
        {
            path: '/',
            component: () => import('./components/main.vue')
        }
    ]
})

export default router

const app = createApp(App)
app.use(pinia).use(Antd).use(router).mount('#app')
