package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Question;

import java.util.List;

public class QuestionJdbcClientRepository implements QuestionRepository {
    @Override
    public List<Question> findQuestionsBySubject(int subjectId) {
        return List.of();
    }

    @Override
    public List<Question> findQuestionsByProfessor(int professorId) {
        return List.of();
    }

    @Override
    public Question addQuestion(Question question) {
        return null;
    }

    @Override
    public boolean updateQuestion(Question question) {
        return false;
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        return false;
    }
}
