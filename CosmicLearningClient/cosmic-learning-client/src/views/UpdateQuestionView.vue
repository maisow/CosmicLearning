<template>
  <div
    class="update-question d-flex justify-content-center align-items-start min-vh-100 py-5"
  >
    <div
      class="login-form rounded shadow p-5"
      style="max-width: 700px; width: 100%; font-size: 1.8rem"
    >
      <h1>Edit Question</h1>

      <form @submit.prevent="handleUpdateQuestion">
        <div>
          <label for="question">Question</label>
          <input v-model="questionText" id="question" type="text" required />
        </div>

        <div>
          <label for="hint">Hint</label>
          <input v-model="hint" id="hint" type="text" required />
        </div>

        <button type="submit">Update Question</button>
      </form>

      <h2>Edit Answer Options</h2>
      <form
        class="answer-option-form"
        v-for="(option, index) in answerOptions"
        :key="option.answerOptionId"
        @submit.prevent="updateOption(index)"
      >
        <div>
          <label>Answer Option</label>
          <input v-model="option.optionAnswer" type="text" required />
        </div>

        <div>
          <label>
            <input v-model="option.correct" type="checkbox" />
            Correct?
          </label>
        </div>

        <button type="submit">Update Option</button>
      </form>

      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">Update successful!</p>

      <router-link to="/questions">
        <button style="margin-top: 20px">Back to Questions</button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useAuth } from "../auth/useAuth";
import { fetchQuestionById, updateQuestion } from "../api/QuestionApi";
import {
  fetchAnswerOptionsByQuestion,
  updateAnswerOption,
} from "../api/AnswerOptionsApi";

const { token } = useAuth();
const route = useRoute();

const questionId = route.params.id;
const questionText = ref("");
const hint = ref("");
const answerOptions = ref([]);
const error = ref(null);
const success = ref(false);

onMounted(async () => {
  try {
    const question = await fetchQuestionById(questionId);
    questionText.value = question.question;
    hint.value = question.hint;

    const options = await fetchAnswerOptionsByQuestion(questionId, token.value);
    answerOptions.value = options;
  } catch (err) {
    error.value = "Failed to load question or options.";
  }
});

async function handleUpdateQuestion() {
  success.value = false;
  error.value = null;

  const payload = {
    questionId: parseInt(questionId),
    question: questionText.value,
    hint: hint.value,
  };

  try {
    const response = await updateQuestion(payload, token.value);
    if (response.error) {
      error.value = response.error;
    } else {
      success.value = true;
    }
  } catch (err) {
    error.value = "Failed to update question.";
  }
}

async function updateOption(index) {
  const option = answerOptions.value[index];

  const payload = {
    answerOptionId: option.answerOptionId,
    optionAnswer: option.optionAnswer,
    correct: option.correct,
    question: {
      questionId: parseInt(questionId),
    },
  };

  try {
    const response = await updateAnswerOption(payload, token.value);
    if (response.error) {
      error.value = response.error;
    } else {
      success.value = true;
    }
  } catch (err) {
    error.value = "Failed to update answer option.";
  }
}
</script>

<style scoped>
.answer-option-form {
  margin-top: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 12px;
  background-color: #f8f9fa;
}

.answer-option-form input[type="text"] {
  width: 100%;
  padding: 12px;
  font-size: 1.1rem;
  border-radius: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
}

.answer-option-form label {
  font-weight: bold;
  display: block;
  margin-bottom: 6px;
  font-size: 1.1rem;
}

.answer-option-form input[type="checkbox"] {
  margin-right: 8px;
}

.answer-option-form button {
  margin-top: 10px;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 1rem;
}
</style>
