<template>
  <div
    class="profile-form d-flex justify-content-center align-items-center vh-100"
  >
    <div
      class="login-form bg-white rounded shadow p-5"
      style="max-width: 700px; width: 100%; font-size: 1.2rem"
    >
      <h1>Cosmic Profile</h1>

      <form @submit.prevent="handleSubmit">
        <div>
          <label for="firstName">First Name</label>
          <input v-model="firstName" id="firstName" type="text" required />
        </div>

        <div>
          <label for="lastName">Last Name</label>
          <input v-model="lastName" id="lastName" type="text" required />
        </div>

        <div>
          <label for="age">Age</label>
          <input
            v-model.number="age"
            id="age"
            type="number"
            min="18"
            required
          />
        </div>

        <div>
          <label for="title">Title</label>
          <input v-model="title" id="title" type="text" />
        </div>

        <div>
          <label for="university">University</label>
          <input v-model="university" id="university" type="text" required />
        </div>

        <button type="submit">
          {{ isExistingProfile ? "Update Profile" : "Create Profile" }}
        </button>

        <p v-if="error" class="error">{{ error }}</p>
        <p v-if="success" class="success">Time to Learn!</p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useRouter } from "vue-router";
import { useAuth } from "../auth/useAuth.js";
import {
  createProfile,
  updateProfile,
  fetchProfileById,
} from "../api/UserApi.js";

const { user, token } = useAuth();
const router = useRouter();

const firstName = ref("");
const lastName = ref("");
const age = ref(18);
const title = ref("");
const university = ref("");

const error = ref(null);
const success = ref(false);
const isExistingProfile = ref(false);
const profileId = ref(null);

console.log("Logged-in user:", user.value);

watch(
  () => user.value,
  async (newUser) => {
    if (newUser && newUser.appUserId) {
      try {
        const profile = await fetchProfileById(newUser.appUserId);

        firstName.value = profile.firstName;
        lastName.value = profile.lastName;
        age.value = profile.age;
        title.value = profile.title;
        university.value = profile.university;

        profileId.value = profile.userId;
        isExistingProfile.value = true;

        console.log("Current user:", newUser);
        console.log("AppUserId:", newUser.appUserId);
      } catch (err) {
        console.log("No Cosmic Profile Found:( Create a New One!");
      }
    }
  },
  { immediate: true }
);

async function handleSubmit() {
  error.value = null;
  success.value = false;

  if (!user.value || !user.value.appUserId) {
    error.value = "User not authenticated.";
    return;
  }

  if (!token.value) {
    error.value = "Authorization Token is Required";
    return;
  }

  const profileData = {
    userId: profileId.value ?? 0,
    firstName: firstName.value,
    lastName: lastName.value,
    age: age.value,
    title: title.value,
    university: university.value,
    amount: 0,
    appUserId: user.value.appUserId,
  };

  try {
    if (isExistingProfile.value) {
      const result = await updateProfile(profileData, token.value);
      if (Array.isArray(result)) throw new Error(result.join(", "));
    } else {
      const result = await createProfile(profileData, token.value);
      if (Array.isArray(result)) throw new Error(result.join(", "));
    }

    success.value = true;
    router.push("/");
  } catch (err) {
    error.value = err.message || "Something Went Wrong:(";
  }
}
</script>

<style scoped>
.profile-form {
  background-image: url("../assets/profile.gif");
  background-size: contain;
  background-position: 90% center;
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
