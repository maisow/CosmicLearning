package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Response;

public interface ResponseRepository {

    Response findAnswer(int userId, int questionId, int answerOptionId, String username);
}
