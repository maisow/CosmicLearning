package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Question;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionJdbcClientRepository implements QuestionRepository {

    private final JdbcClient jdbcClient;

    public QuestionJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Question> findQuestionsBySubject(int subjectId, String username) {
        List<String> roles = getRolesByUsername(username);

        String sql = """
        select
            u.user_id,
            u.first_name,
            u.last_name,
            u.age,
            u.title,
            u.university,
            u.amount,
            u.app_user_id,
            s.subject_id,
            s.name,
            s.description,
            q.question_id,
            q.question,
            q.hint
        from question q
        join subject s on q.subject_id = s.subject_id
        join user u on q.user_id = u.user_id
        where s.subject_id = ?;
        """;

        return jdbcClient.sql(sql)
                .param(subjectId)
                .query(new QuestionMapper(roles))
                .list();
    }

    @Override
    public List<Question> findQuestionsByProfessor(int professorId, String username) {
        List<String> roles = getRolesByUsername(username);

        String sql = """
        select
            u.user_id,
            u.first_name,
            u.last_name,
            u.age,
            u.title,
            u.university,
            u.amount,
            u.app_user_id,
            s.subject_id,
            s.name,
            s.description,
            q.question_id,
            q.question,
            q.hint
        from question q
        join subject s on q.subject_id = s.subject_id
        join user u on q.user_id = u.user_id
        where u.user_id = ?;
        """;

        return jdbcClient.sql(sql)
                .param(professorId)
                .query(new QuestionMapper(roles))
                .list();
    }

    @Override
    public Question addQuestion(Question question) {
        final String sql = "insert into question (question, hint, user_id, subject_id) "
                + "values (:question, :hint, :user_id, :subject_id);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("question", question.getQuestion())
                .param("hint", question.getHint())
                .param("user_id", question.getProfessor().getUserId())
                .param("subject_id", question.getSubject().getSubjectId())
                .update(keyHolder, "question_id");

        if (rowsAffected <= 0) {
            return null;
        }

        question.setQuestionId(keyHolder.getKey().intValue());

        return question;
    }

    @Override
    public boolean updateQuestion(Question question) {
        final String sql = "update question set "
                + "question = :question, "
                + "hint = :hint, "
                + "user_id = :user_id, "
                + "subject_id = :subject_id "
                + "where question_id = :question_id;";


        return jdbcClient.sql(sql)
                .param("question", question.getQuestion())
                .param("hint", question.getHint())
                .param("user_id", question.getProfessor().getUserId())
                .param("subject_id", question.getSubject().getSubjectId())
                .param("question_id", question.getQuestionId())
                .update() > 0;
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        jdbcClient.sql("delete from response where question_id = ?;").param(questionId).update();
        jdbcClient.sql("delete from answer_options where question_id = ?;").param(questionId).update();
        return jdbcClient.sql("delete from question where question_id = ?;").param(questionId).update() > 0;
    }

    @Override
    public Question findById(int questionId, String username) {
        List<String> roles = getRolesByUsername(username);

        final String sql = """
        select
            u.user_id,
            u.first_name,
            u.last_name,
            u.age,
            u.title,
            u.university,
            u.amount,
            u.app_user_id,
            s.subject_id,
            s.name,
            s.description,
            q.question_id,
            q.question,
            q.hint
        from question q
        join subject s on q.subject_id = s.subject_id
        join user u on q.user_id = u.user_id
        where q.question_id = ?;
    """;

        return jdbcClient.sql(sql)
                .param(questionId)
                .query(new QuestionMapper(roles))
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
