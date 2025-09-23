package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AnswerOptions;

public class AnswerOptionsJdbcClientRepository implements AnswerOptionsRepository {
    @Override
    public AnswerOptions createAnswerOptions(AnswerOptions answerOptions) {
        return null;
    }

    @Override
    public AnswerOptions getAnswerOptions(int questionId) {
        return null;
    }

    @Override
    public boolean updateAnswerOptions(AnswerOptions answerOptions) {
        return false;
    }

    @Override
    public boolean deleteAnswerOptions(int questionId) {
        return false;
    }
}
