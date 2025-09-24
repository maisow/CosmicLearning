package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Response;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResponseJdbcClientRepository implements ResponseRepository {

    private final JdbcClient jdbcClient;

    public ResponseJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Response findAnswer(int userId, int questionId, int answerOptionId, String username) {

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
            ao.option_answer,
            ao.correct
        from response r
        join user u on r.user_id = u.user_id
        join app_user ap on u.app_user_id = ap.app_user_id
        join question q on r.question_id = q.question_id
        join subject s on q.subject_id = s.subject_id
        join answer_options ao on r.answer_option_id = ao.answer_option_id
        where r.user_id = ? and r.question_id = ? and r.answer_option_id = ?;
        """;

        return jdbcClient.sql(sql)
                .param(userId)
                .param(questionId)
                .param(answerOptionId)
                .query(new ResponseMapper(roles))
                .optional()
                .orElse(null);
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
