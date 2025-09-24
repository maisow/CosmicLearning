package learn.cosmiclearning.models;

import java.util.Objects;

public class AnswerOptions {

    private int answerOptionId;
    private String optionAnswer;
    private boolean isCorrect;
    private Question question;

    public AnswerOptions(int answerOptionId, String optionAnswer, boolean isCorrect, Question question) {
        this.answerOptionId = answerOptionId;
        this.isCorrect = isCorrect;
        this.optionAnswer = optionAnswer;
        this.question = question;
    }

    public int getAnswerOptionId() {
        return answerOptionId;
    }

    public void setAnswerOptionId(int answerOptionId) {
        this.answerOptionId = answerOptionId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getOptionAnswer() {
        return optionAnswer;
    }

    public void setOptionAnswer(String optionAnswer) {
        this.optionAnswer = optionAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AnswerOptions that)) return false;
        return getAnswerOptionId() == that.getAnswerOptionId() && isCorrect() == that.isCorrect() && Objects.equals(getOptionAnswer(), that.getOptionAnswer()) && Objects.equals(getQuestion(), that.getQuestion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnswerOptionId(), getOptionAnswer(), isCorrect(), getQuestion());
    }

    @Override
    public String toString() {
        return "AnswerOptions{" +
                "answerOptionId=" + answerOptionId +
                ", optionAnswer='" + optionAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                ", question=" + question +
                '}';
    }
}
