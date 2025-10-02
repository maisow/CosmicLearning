<template>
  <div class="reviews d-flex justify-content-center align-items-center vh-100">
    <h1>Professor Reviews</h1>
    <div>
      <label for="professor">Select Professor</label>
      <select v-model="selectedProfessorId">
        <option disabled value="">Select Professor</option>
        <option
          v-for="prof in professors"
          :key="prof.appUserId"
          :value="prof.appUserId"
        >
          {{ prof.firstName }} {{ prof.lastName }}
        </option>
      </select>
      <button @click="fetchReviews">Search</button>
    </div>

    <div v-if="selectedProfessor">
      <div class="professor-card">
        <h2>
          {{ selectedProfessor.title }} {{ selectedProfessor.firstName }}
          {{ selectedProfessor.lastName }}
        </h2>
        <p><strong>University:</strong> {{ selectedProfessor.university }}</p>
        <p><strong>Age:</strong> {{ selectedProfessor.age }}</p>
      </div>

      <div v-if="isStudent">
        <router-link :to="`/reviews/add/${selectedProfessorId}`">
          <button>Add Review</button>
        </router-link>
      </div>
    </div>

    <div v-if="reviews.length">
      <h3>Reviews:</h3>
      <ul>
        <li
          v-for="review in reviews"
          :key="review.reviewId"
          class="review-item"
        >
          <p><strong>Review:</strong> {{ review.description }}</p>
          <p>
            <strong>Professor:</strong> {{ review.professor.firstName }}
            {{ review.professor.lastName }}
          </p>
          <p><strong>Date:</strong> {{ formatDate(review.reviewDate) }}</p>
        </li>
      </ul>
    </div>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuth } from "../auth/useAuth";
import { fetchAllUsers } from "../api/UserApi";
import { fetchReviewsByProfessorId } from "../api/ReviewsApi";

const { user, token } = useAuth();

const professors = ref([]);
const selectedProfessorId = ref("");
const selectedProfessor = ref(null);
const reviews = ref([]);
const error = ref(null);

const isStudent = user.value?.authorities.includes("STUDENT");

onMounted(async () => {
  if (token.value) {
    try {
      professors.value = await fetchAllUsers(token.value);
    } catch (err) {
      console.error("Error loading dropdown:", err);
    }
  }
});

async function fetchReviews() {
  error.value = null;
  reviews.value = [];
  selectedProfessor.value = null;

  try {
    selectedProfessor.value = professors.value.find(
      (p) => p.appUserId === selectedProfessorId.value
    );

    const reviewList = await fetchReviewsByProfessorId(
      selectedProfessorId.value
    );
    console.log("Fetched reviews:", reviewList);
    reviews.value = reviewList;
  } catch (err) {
    error.value = "Failed to load reviews.";
  }
}

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleDateString();
}
</script>

<style scoped>
.reviews {
  background-image: url("../assets/stars.gif");
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
