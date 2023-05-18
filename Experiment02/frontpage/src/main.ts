import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css';
import {createRouter, createWebHistory} from "vue-router";
import {createPinia} from "pinia";

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

createApp(App).use(Antd).use(router).use(createPinia).mount('#app')
