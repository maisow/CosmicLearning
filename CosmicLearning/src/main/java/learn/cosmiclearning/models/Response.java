package learn.cosmiclearning.models;

import java.util.Objects;

public class Response {

    private Question question;
    private User user;
    private AnswerOptions answerOptions;

    public Response(Question question, User user, AnswerOptions answerOptions) {
        this.answerOptions = answerOptions;
        this.question = question;
        this.user = user;
    }

    public AnswerOptions getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(AnswerOptions answerOptions) {
        this.answerOptions = answerOptions;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Response response)) return false;
        return Objects.equals(getQuestion(), response.getQuestion()) && Objects.equals(getUser(), response.getUser()) && Objects.equals(getAnswerOptions(), response.getAnswerOptions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestion(), getUser(), getAnswerOptions());
    }

    @Override
    public String toString() {
        return "Response{" +
                "answerOptions=" + answerOptions +
                ", question=" + question +
                ", user=" + user +
                '}';
    }
}
