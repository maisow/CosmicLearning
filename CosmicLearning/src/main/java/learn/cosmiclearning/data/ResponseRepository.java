package learn.cosmiclearning.data;

public interface ResponseRepository {

    boolean answer(int userId, int questionId, int answerOptionId);
}
