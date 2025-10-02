<template>
  <nav class="nav-bar">
    <router-link to="/" class="nav-content">
      <img
        src="@/assets/albert-logo.jpg"
        alt="Cosmic Learning Logo"
        class="logo"
      />
    </router-link>
    <router-link to="/join-view" v-if="!isLoggedIn">
      <button class="custom-nav-btn">Login</button>
    </router-link>
    <router-link to="/register" v-if="!isLoggedIn">
      <button class="custom-nav-btn">Register</button>
    </router-link>
    <router-link to="/profile" v-if="isLoggedIn">
      <button class="custom-nav-btn">Cosmic Profile</button>
    </router-link>
    <router-link to="/questions" v-if="isLoggedIn">
      <button class="custom-nav-btn">Questions</button>
    </router-link>
    <router-link to="/reviews" v-if="isLoggedIn">
      <button class="custom-nav-btn">Reviews</button>
    </router-link>
    <button v-if="isLoggedIn" @click="logout" class="custom-nav-btn">
      Logout
    </button>
  </nav>
  <router-view />
</template>

<script setup>
import { useAuth } from "@/auth/useAuth.js";
import { useRouter } from "vue-router";

const router = useRouter();
const { isLoggedIn, logout: authLogout } = useAuth();

function logout() {
  authLogout();
  router.push("/");
}
</script>

<style lang="scss">
.nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 180px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px 10px;
  gap: 15px;
}

.nav-content {
  color: white;
  font-family: papyrus;
  font-size: 30px;
}

.logo {
  height: 100px;
  width: auto;
}

.custom-nav-btn {
  background-color: #b4d9f0;
  color: white;
  border: none;
  border-radius: 25px;
  padding: 15px 30px;
  font-size: 1.4rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 90%;
  text-align: center;
}

.custom-nav-btn:hover {
  background-color: #66b2e8;
}
</style>
