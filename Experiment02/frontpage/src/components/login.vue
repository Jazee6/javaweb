<template>
    <div style="padding: 16px;">
        <a-space direction="vertical" style="display: flex;flex-direction: column;align-items: center">
            <a-input v-model:value="username" placeholder="请输入用户名" style="min-width: 512px"/>
            <a-input-password v-model:value="password" placeholder="请输入密码" style="min-width: 512px"/>
            <a-button type="primary" @click="login">登录</a-button>
        </a-space>
    </div>
</template>

<script setup>
import {ref} from "vue";
import {message} from "ant-design-vue";

const username = ref()
const password = ref()

const login = () => {
    if (!username.value) return message.error("用户名不能为空")
    if (!password.value) return message.error("密码不能为空")

    fetch("http://localhost:5173/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: username.value,
            password: password.value
        })
    }).then(r => r.text()).then(r =>{
        if (r !== "success") {
            message.error(r)
        }else {
            message.success("登录成功！")
            window.location.href = "/"
        }
    })
}
</script>