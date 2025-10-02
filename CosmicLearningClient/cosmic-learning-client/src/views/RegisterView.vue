<template>
  <div
    class="user-registration d-flex justify-content-center align-items-center vh-100"
  >
    <div
      class="login-form bg-white rounded shadow p-5"
      style="max-width: 700px; width: 100%; font-size: 1.2rem"
    >
      <h1 class="text-center mb-4">Join Cosmic Learning!</h1>

      <form @submit.prevent="handleRegister">
        <div class="mb-3">
          <label for="username" class="form-label">Username</label>
          <input
            v-model="username"
            type="email"
            id="username"
            required
            class="form-control form-control-lg"
          />
        </div>

        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input
            v-model="password"
            type="password"
            id="password"
            required
            class="form-control form-control-lg"
          />
        </div>

        <div class="mb-4">
          <label for="role" class="form-label">Role</label>
          <select
            v-model="selectedRole"
            id="role"
            required
            class="form-select form-select-lg"
          >
            <option disabled value="">Who Are You?</option>
            <option value="STUDENT">STUDENT</option>
            <option value="PROFESSOR">PROFESSOR</option>
          </select>
        </div>

        <button type="submit" class="btn btn-primary btn-lg w-100">
          Register
        </button>

        <p v-if="error" class="text-danger mt-3 text-center">{{ error }}</p>
        <p v-if="isSuccess" class="text-success mt-3 text-center">
          Welcome to Cosmic Learning!
        </p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { register, authenticate } from "../api/AuthContext.js";
import { useAuth } from "../auth/useAuth.js";

const { login } = useAuth();
const username = ref("");
const password = ref("");
const selectedRole = ref("");
const error = ref(null);
const isSuccess = ref(false);
const router = useRouter();

async function handleRegister() {
  error.value = null;
  isSuccess.value = false;

  try {
    await register(username.value, password.value, [selectedRole.value]);

    await authenticate({ username: username.value, password: password.value });

    login(localStorage.getItem("jwt_token"));

    isSuccess.value = true;
    router.push("/");
  } catch (err) {
    if (Array.isArray(err)) {
      error.value = err.join(", ");
    } else {
      error.value = "Sorry:( something went wrong...)";
    }
  }
}
</script>

<style scoped>
.user-registration {
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
