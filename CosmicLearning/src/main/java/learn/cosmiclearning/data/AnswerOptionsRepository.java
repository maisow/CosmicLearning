package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AnswerOptions;

import java.util.List;

public interface AnswerOptionsRepository {

    AnswerOptions createAnswerOptions(AnswerOptions answerOptions);

    List<AnswerOptions> getAnswerOptionsByQuestion(int questionId, String username);

    boolean updateAnswerOptions(AnswerOptions answerOptions);

    boolean deleteAnswerOptions(int answerOptionsId);
}
