<template>
  <div
    class="questions d-flex justify-content-center align-items-center vh-100"
  >
    <div
      class="login-form rounded shadow p-5"
      style="max-width: 850px; width: 100%; font-size: 1.8rem"
    >
      <h1>Questions</h1>
      <div>
        <label>Select Subject</label>
        <select v-model="selectedSubjectId">
          <option disabled value="">Select Subject</option>
          <option
            v-for="subject in subjects"
            :key="subject.subjectId"
            :value="subject.subjectId"
          >
            {{ subject.name }}
          </option>
        </select>
        <button @click="searchBySubject">Search</button>
      </div>

      <div>
        <label>Select Professor</label>
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
        <button @click="searchByProfessor">Search</button>
      </div>

      <div v-if="isProfessor">
        <router-link to="/questions/add">
          <button>Add Question</button>
        </router-link>
      </div>

      <div v-if="questions.length">
        <h3>Questions Found:</h3>
        <ul>
          <li v-for="q in questions" :key="q.questionId">
            {{ q.question }}
            <router-link
              v-if="isProfessor"
              :to="`/questions/edit/${q.questionId}`"
              style="margin-left: 10px"
            >
              <button>Edit</button>
            </router-link>

            <router-link
              v-if="!isProfessor"
              :to="`/questions/answer/${q.questionId}`"
              style="margin-left: 10px"
            >
              <button>Answer</button>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { fetchByProfessorId, fetchBySubjectId } from "../api/QuestionApi";
import { fetchAllUsers } from "../api/UserApi";
import { fetchAllSubjects } from "../api/SubjectApi";
import { useAuth } from "../auth/useAuth";

const { user, token } = useAuth();
console.log("JWT Token:", token.value);
const isProfessor = user.value?.authorities.includes("PROFESSOR");

const selectedSubjectId = ref("");
const selectedProfessorId = ref("");

const questions = ref([]);
const professors = ref([]);
const subjects = ref([]);

async function searchBySubject() {
  try {
    questions.value = await fetchBySubjectId(selectedSubjectId.value);
  } catch {
    alert("No questions found for this subject.");
  }
}

async function searchByProfessor() {
  try {
    questions.value = await fetchByProfessorId(selectedProfessorId.value);
  } catch {
    alert("No questions found for this professor.");
  }
}

onMounted(async () => {
  if (token.value) {
    try {
      professors.value = await fetchAllUsers(token.value);
      console.log("Professors loaded:", professors.value);
      subjects.value = await fetchAllSubjects(token.value);
      console.log("Subjects loaded:", subjects.value);
    } catch (err) {
      console.error("Error loading dropdowns:", err);
    }
  }
});
</script>

<style scoped>
.questions {
  background-image: url("../assets/space.gif");
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
