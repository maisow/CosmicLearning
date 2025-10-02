<template>
  <div
    class="user-profile d-flex justify-content-center align-items-center vh-100"
  >
    <div
      class="login-form bg-white rounded shadow p-5"
      style="max-width: 700px; width: 100%; font-size: 1.2rem"
    >
      <h1>Welcome Back!</h1>
      <h2>Login</h2>
      <form @submit.prevent="handleLogin">
        <div>
          <label for="username">Username</label>
          <input v-model="username" id="username" type="text" required />
        </div>

        <div>
          <label for="password">Password</label>
          <input v-model="password" id="password" type="password" required />
        </div>

        <div>
          <button type="submit">Login</button>
        </div>

        <p v-if="error" class="error">{{ error }}</p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { authenticate } from "../api/AuthContext.js";
import { useAuth } from "../auth/useAuth.js";

const { login } = useAuth();

const username = ref("");
const password = ref("");
const error = ref(null);
const router = useRouter();

async function handleLogin() {
  error.value = null;
  try {
    await authenticate({
      username: username.value,
      password: password.value,
    });

    login(localStorage.getItem("jwt_token"));

    router.push("/");
  } catch {
    error.value = "Invalid credentials";
  }
}
</script>

<style scoped>
.user-profile {
  background-image: url("../assets/login.gif");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  padding-top: 40px;
}
</style>
