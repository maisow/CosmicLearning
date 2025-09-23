package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AnswerOptions;

public interface AnswerOptionsRepository {

    AnswerOptions createAnswerOptions(AnswerOptions answerOptions);

    AnswerOptions getAnswerOptions(int questionId);

    boolean updateAnswerOptions(AnswerOptions answerOptions);

    boolean deleteAnswerOptions(int questionId);
}
