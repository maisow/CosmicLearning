<template>
  <div
    class="answer-question d-flex justify-content-center align-items-center vh-100"
  >
    <div
      class="login-form rounded shadow p-5"
      style="max-width: 850px; width: 100%; font-size: 1.8rem"
    >
      <h1>Answer Question</h1>

      <div v-if="question">
        <h2>{{ question.question }}</h2>

        <ul>
          <li v-for="option in answerOptions" :key="option.answerOptionId">
            <label>
              <input
                type="radio"
                name="selectedOption"
                :value="option.answerOptionId"
                v-model="selectedOptionId"
              />
              {{ option.optionAnswer }}
            </label>
          </li>
        </ul>

        <button @click="submitAnswer" :disabled="!selectedOptionId">
          Submit
        </button>

        <div v-if="feedback">
          <p :class="{ correct: isCorrect, incorrect: !isCorrect }">
            {{ feedback }}
          </p>
          <router-link to="/questions">
            <button>Back to Questions</button>
          </router-link>
        </div>
      </div>

      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useAuth } from "../auth/useAuth";
import { fetchQuestionById } from "../api/QuestionApi";
import { fetchAnswerOptionsByQuestion } from "../api/AnswerOptionsApi";
import { saveResponse, isAnswerCorrect } from "../api/ResponseApi";

const route = useRoute();
const { user, token } = useAuth();

const question = ref(null);
const answerOptions = ref([]);
const selectedOptionId = ref(null);
const feedback = ref("");
const isCorrect = ref(false);
const error = ref(null);

onMounted(async () => {
  const questionId = route.params.id;

  try {
    const data = await fetchQuestionById(questionId);
    question.value = data;

    const options = await fetchAnswerOptionsByQuestion(questionId, token.value);
    console.log(options);
    if (options.error) {
      error.value = options.error;
    } else {
      answerOptions.value = options;
    }
  } catch (err) {
    error.value = "Could not load question or answer options.";
  }
});

async function submitAnswer() {
  if (!selectedOptionId.value) return;

  try {
    const correctCheck = await isAnswerCorrect(
      selectedOptionId.value,
      token.value
    );
    isCorrect.value = correctCheck.correct;

    const responsePayload = {
      user: {
        appUserId: user.value.appUserId,
      },
      question: {
        questionId: question.value.questionId,
      },
      answerOption: {
        answerOptionId: selectedOptionId.value,
      },
    };

    await saveResponse(responsePayload, token.value);

    feedback.value = isCorrect.value ? "Correct!" : "Incorrect";
  } catch (err) {
    error.value = "Something went wrong.";
  }
}
</script>

<style scoped>
.answer-question {
  background-image: url("../assets/whitemark.png");
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
