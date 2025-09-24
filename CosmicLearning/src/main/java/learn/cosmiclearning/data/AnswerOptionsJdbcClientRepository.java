package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AnswerOptions;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;

public class AnswerOptionsJdbcClientRepository implements  AnswerOptionsRepository {


    private final JdbcClient jdbcClient;

    public AnswerOptionsJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public AnswerOptions createAnswerOptions(AnswerOptions answerOptions) {
        final String sql = "insert into answer_options (option_answer, correct, question_id) "
                + "values (:option_answer, :correct, :question_id);";


        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();


        int rowsAffected = jdbcClient.sql(sql)
                .param("option_answer", answerOptions.getOptionAnswer())
                .param("correct", answerOptions.isCorrect())
                .param("question_id", answerOptions.getQuestion().getQuestionId())
                .update(keyHolder, "answer_option_id");


        if (rowsAffected <= 0) {
            return null;
        }


        answerOptions.setAnswerOptionId(keyHolder.getKey().intValue());


        return answerOptions;
    }

    @Override
    public List<AnswerOptions> getAnswerOptionsByQuestion(int questionId, String username) {
        List<String> roles = getRolesByUsername(username);

        String sql = """
        select
            u.user_id,
            u.first_name,
            u.last_name,
            u.age,
            u.title,
            u.university,
            u.email,
            u.amount,
            ap.app_user_id,
            ap.username,
            ap.password_hash,
            ap.enabled,
            s.subject_id,
            s.name,
            s.description,
            q.question_id,
            q.question,
            q.hint,
            ao.answer_option_id,
            ao.option,
            ao.correct,
        from answer_options ao
        join question q on ao.question_id = q.question_id
        join subject s on q.subject_id = s.subject_id
        join user u on q.user_id = u.user_id
        join app_user ap on u.app_user_id = ap.app_user_id
        where q.question_id = ?;
        """;

        return jdbcClient.sql(sql)
                .param(questionId)
                .query(new AnswerOptionsMapper(roles))
                .list();
    }

    @Override
    public boolean updateAnswerOptions(AnswerOptions answerOptions) {
        final String sql = "update answer_options set "
                + "option = :option, "
                + "correct = :correct, "
                + "question_id = :question_id, "
                + "where answer_option_id = :answer_option_id;";


        return jdbcClient.sql(sql)
                .param("option", answerOptions.getOptionAnswer())
                .param("correct", answerOptions.isCorrect())
                .param("question_id", answerOptions.getQuestion().getQuestionId())
                .param("answer_option_id", answerOptions.getAnswerOptionId())
                .update() > 0;
    }

    @Override
    public boolean deleteAnswerOptions(int answerOptionsId) {
        jdbcClient.sql("delete from response where answer_option_id = ?;").param(answerOptionsId).update();
        return jdbcClient.sql("delete from answer_options where answer_option_id = ?;").param(answerOptionsId).update() > 0;
    }

    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "join app_role r on ur.app_role_id = r.app_role_id "
                + "join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.username = ?";
        return jdbcClient.sql(sql)
                .param(username)
                .query((rs, rowId) -> rs.getString("name"))
                .list();
    }
}
