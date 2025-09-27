package learn.cosmiclearning.models;

import java.util.Objects;

public class Response {

    private int responseId;
    private Question question;
    private User user;
    private AnswerOptions answerOptions;

    public Response(int responseId, Question question, User user, AnswerOptions answerOptions) {
        this.responseId = responseId;
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


    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Response response)) return false;
        return getResponseId() == response.getResponseId() && Objects.equals(getQuestion(), response.getQuestion()) && Objects.equals(getUser(), response.getUser()) && Objects.equals(getAnswerOptions(), response.getAnswerOptions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResponseId(), getQuestion(), getUser(), getAnswerOptions());
    }

    @Override
    public String toString() {
        return "Response{" +
                "answerOptions=" + answerOptions +
                ", responseId=" + responseId +
                ", question=" + question +
                ", user=" + user +
                '}';
    }
}
