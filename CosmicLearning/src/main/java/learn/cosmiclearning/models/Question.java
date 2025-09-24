package learn.cosmiclearning.models;

import java.util.Objects;

public class Question {

    private int questionId;
    private String question;
    private String hint;
    private User professor;
    private Subject subject;

    public Question(int questionId, String question, String hint, Subject subject, User professor) {
        this.hint = hint;
        this.professor = professor;
        this.question = question;
        this.questionId = questionId;
        this.subject = subject;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Question question1)) return false;
        return getQuestionId() == question1.getQuestionId() && Objects.equals(getQuestion(), question1.getQuestion()) && Objects.equals(getHint(), question1.getHint()) && Objects.equals(getProfessor(), question1.getProfessor()) && Objects.equals(getSubject(), question1.getSubject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestionId(), getQuestion(), getHint(), getProfessor(), getSubject());
    }

    @Override
    public String toString() {
        return "Question{" +
                "hint='" + hint + '\'' +
                ", questionId=" + questionId +
                ", question='" + question + '\'' +
                ", professor=" + professor +
                ", subject=" + subject +
                '}';
    }
}
