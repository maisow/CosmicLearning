import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import JoinView from "../views/JoinView.vue";
import RegisterView from "../views/RegisterView.vue";
import ProfileView from "../views/ProfileView.vue";
import QuestionsView from "../views/QuestionsView.vue";
import AddQuestionView from "../views/AddQuestionView.vue";
import AnswerQuestionView from "../views/AnswerQuestionView.vue";
import UpdateQuestionView from "../views/UpdateQuestionView.vue";
import ReviewsView from "../views/ReviewsView.vue";
import AddReviewView from "../views/AddReviewView.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/join-view",
    name: "joinview",
    component: JoinView,
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
  },
  {
    path: "/profile",
    name: "profile",
    component: ProfileView,
  },
  {
    path: "/questions",
    component: QuestionsView,
  },
  {
    path: "/questions/add",
    component: AddQuestionView,
  },
  {
    path: "/questions/answer/:id",
    name: "answer-question",
    component: AnswerQuestionView,
    props: true,
  },
  {
    path: "/questions/edit/:id",
    name: "edit-question",
    component: UpdateQuestionView,
    props: true,
  },
  {
    path: "/reviews",
    name: "reviews",
    component: ReviewsView,
  },
  {
    path: "/reviews/add/:professorId",
    name: "add-review",
    component: AddReviewView,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
