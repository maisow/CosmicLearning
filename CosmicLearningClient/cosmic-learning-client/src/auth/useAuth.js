import { ref, computed } from "vue";

const token = ref(localStorage.getItem("jwt_token"));

function parseToken(token) {
  if (!token) return null;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    payload.authorities = payload.authorities?.split(",") || [];

    if (payload.app_user_id) {
      payload.appUserId = payload.app_user_id;
    }

    return payload;
  } catch {
    return null;
  }
}

export function useAuth() {
  const isLoggedIn = computed(() => !!token.value);

  const user = computed(() => parseToken(token.value));
  function login(newToken) {
    localStorage.setItem("jwt_token", newToken);
    token.value = newToken;
  }

  function logout() {
    localStorage.removeItem("jwt_token");
    token.value = null;
  }

  return {
    isLoggedIn,
    login,
    logout,
    token,
    user,
  };
}
