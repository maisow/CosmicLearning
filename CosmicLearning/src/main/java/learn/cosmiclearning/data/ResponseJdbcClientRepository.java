package learn.cosmiclearning.data;

public class ResponseJdbcClientRepository implements ResponseRepository {
    @Override
    public boolean answer(int userId, int questionId, int answerOptionId) {
        return false;
    }
}
