package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Question;

import java.util.List;

public interface QuestionRepository {

    List<Question> findQuestionsBySubject(int subjectId, String username);

    List<Question> findQuestionsByProfessor(int professorId, String username);

    Question addQuestion(Question question);

    boolean updateQuestion(Question question);

    boolean deleteQuestion(int questionId);

    Question findById(int questionId, String username);
}
