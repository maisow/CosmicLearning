<template>
  <div
    class="add-question d-flex justify-content-center align-items-center vh-100"
  >
    <div
      class="login-form rounded shadow p-5"
      style="max-width: 850px; width: 100%; font-size: 1.8rem"
    >
      <h1>Create Question</h1>

      <form @submit.prevent="handleSubmit">
        <div>
          <label for="question">Question</label>
          <input v-model="question" id="question" type="text" required />
        </div>

        <div>
          <label for="hint">Hint</label>
          <input v-model="hint" id="hint" type="text" required />
        </div>

        <div>
          <label for="subject">Select Question Subject</label>
          <select v-model="selectedSubjectId" required>
            <option disabled value="">Select Subject</option>
            <option
              v-for="subject in subjects"
              :key="subject.subjectId"
              :value="subject.subjectId"
            >
              {{ subject.name }}
            </option>
          </select>
        </div>

        <button type="submit">Create Question</button>

        <p v-if="error" class="error">{{ error }}</p>
        <p v-if="success" class="success">Question created successfully!</p>
      </form>

      <form @submit.prevent="handleSubmitOptions" v-if="createdQuestionId">
        <h2>Add Answer Options</h2>

        <div>
          <label for="optionAnswer">Answer Option</label>
          <input
            v-model="optionAnswer"
            id="optionAnswer"
            type="text"
            required
          />
        </div>

        <div>
          <label>
            <input v-model="correct" type="checkbox" />
            Correct?
          </label>
        </div>

        <button type="submit">Add Option</button>

        <p v-if="errorOption" class="error">{{ errorOption }}</p>
        <p v-if="successOption" class="success">Answer option added!</p>
      </form>

      <router-link to="/questions">
        <button style="margin-top: 20px">Back to Questions</button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useAuth } from "../auth/useAuth.js";
import { createQuestion } from "../api/QuestionApi.js";
import { fetchAllSubjects } from "../api/SubjectApi.js";
import { createAnswerOption } from "../api/AnswerOptionsApi.js";
import { fetchProfileById } from "../api/UserApi";

const { user, token } = useAuth();
console.log("Current user:", user.value);
const question = ref("");
const hint = ref("");
const selectedSubjectId = ref("");
const subjects = ref([]);
const error = ref(null);
const success = ref(false);
const createdQuestionId = ref(null);
const profile = ref(null);

const optionAnswer = ref("");
const correct = ref(false);
const successOption = ref(false);
const errorOption = ref(null);

async function loadSubjects() {
  if (!token.value) {
    console.warn("No token available yet, cannot fetch subjects.");
    return;
  }

  try {
    console.log("Fetching subjects using token:", token.value);
    const data = await fetchAllSubjects(token.value);
    console.log("Fetched subjects:", data);
    subjects.value = data;
  } catch (err) {
    console.error("Failed to fetch subjects:", err);
  }
}

onMounted(async () => {
  if (token.value && user.value?.appUserId) {
    await loadSubjects();
    try {
      profile.value = await fetchProfileById(user.value.appUserId);
      console.log("Loaded profile:", profile.value);
    } catch (err) {
      console.error("Failed to load profile:", err);
    }
  }
});
watch(token, (newToken) => {
  if (newToken) {
    loadSubjects();
  }
});

async function handleSubmit() {
  error.value = null;
  success.value = false;
  createdQuestionId.value = null;

  if (!profile.value) {
    error.value = "Profile is still loading. Please try again shortly.";
    return;
  }

  if (!profile.value.userId) {
    error.value = "User ID not found in profile.";
    return;
  }

  const payload = {
    question: question.value,
    hint: hint.value,
    professor: {
      userId: profile.value.userId,
    },
    subject: {
      subjectId: selectedSubjectId.value,
    },
  };

  try {
    const response = await createQuestion(payload, token.value);
    console.log("Question created:", response);

    if (response?.questionId) {
      createdQuestionId.value = response.questionId;
      success.value = true;
    } else {
      error.value = "Failed to create question.";
    }
  } catch (err) {
    console.error("Error submitting question:", err);
    error.value = "Something went wront:(";
  }
}

async function handleSubmitOptions() {
  errorOption.value = null;
  successOption.value = false;

  if (!createdQuestionId.value) {
    errorOption.value = "Please create a question first.";
    return;
  }

  const payload = {
    optionAnswer: optionAnswer.value,
    isCorrect: correct.value,
    question: {
      questionId: createdQuestionId.value,
    },
  };

  try {
    await createAnswerOption(payload, token.value);
    successOption.value = true;
    optionAnswer.value = "";
    correct.value = false;
  } catch (err) {
    console.error("Error submitting answer option:", err);
    errorOption.value =
      "Something went wrong while creating the answer option.";
  }
}
</script>

<style scoped>
.add-question {
  background-image: url("../assets/createQuestion.gif");
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
