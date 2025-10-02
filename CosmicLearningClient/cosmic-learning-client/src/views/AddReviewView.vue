<template>
  <div
    class="add-review d-flex justify-content-center align-items-center vh-100"
  >
    <div
      class="login-form rounded shadow p-5"
      style="max-width: 850px; width: 100%; font-size: 1.8rem"
    >
      <h1>Add Review for {{ professorName }}</h1>

      <form @submit.prevent="handleSubmit">
        <div>
          <label for="description">Review Description</label>
          <textarea
            id="description"
            v-model="description"
            required
            rows="4"
            placeholder="Write your review here"
          ></textarea>
        </div>

        <div>
          <label for="stars">Stars</label>
          <select v-model.number="stars" id="stars" required>
            <option disabled value="">Select stars</option>
            <option v-for="n in 5" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>

        <button type="submit">Submit Review</button>

        <p v-if="error" class="error">{{ error }}</p>
        <p v-if="success" class="success">Review submitted successfully!</p>
      </form>

      <router-link to="/reviews">
        <button style="margin-top: 20px">Back to Reviews</button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuth } from "../auth/useAuth.js";
import { createReview } from "../api/ReviewsApi.js";
import { fetchProfileById } from "../api/UserApi.js";
import { useRoute } from "vue-router";

const { user, token } = useAuth();
const route = useRoute();

const description = ref("");
const stars = ref(null);
const error = ref(null);
const success = ref(false);

const professorId = ref(null);
const professorName = ref("");
const profile = ref(null);

onMounted(async () => {
  professorId.value = Number(route.params.professorId);

  if (!professorId.value) {
    error.value = "No professor selected.";
    return;
  }

  try {
    const prof = await fetchProfileById(professorId.value);
    professorName.value = `${prof.title} ${prof.firstName} ${prof.lastName}`;
  } catch (err) {
    error.value = "Failed to load professor data.";
    console.error(err);
    return;
  }

  try {
    profile.value = await fetchProfileById(user.value.appUserId);
    console.log("Loaded student profile:", profile.value);
  } catch (err) {
    error.value = "Failed to load student profile.";
    console.error(err);
  }
});

async function handleSubmit() {
  error.value = null;
  success.value = false;

  if (!description.value.trim()) {
    error.value = "Description cannot be blank.";
    return;
  }

  if (!stars.value || stars.value < 1 || stars.value > 5) {
    error.value = "Please select a valid number of stars (1-5).";
    return;
  }

  if (!profile.value?.userId) {
    error.value = "You must be logged in to submit a review.";
    return;
  }

  if (!professorId.value) {
    error.value = "Invalid professor ID.";
    return;
  }

  const payload = {
    description: description.value,
    stars: stars.value,
    student: {
      userId: profile.value.userId,
    },
    professor: {
      userId: professorId.value,
    },
  };

  try {
    await createReview(payload, token.value);
    success.value = true;
    description.value = "";
    stars.value = null;
  } catch (err) {
    error.value = "Failed to submit review.";
    console.error(err);
  }
}
</script>

<style scoped>
.add-review {
  background-image: url("../assets/reviews.gif");
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
