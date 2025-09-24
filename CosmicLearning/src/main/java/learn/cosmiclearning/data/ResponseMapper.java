package learn.cosmiclearning.data;

import learn.cosmiclearning.models.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResponseMapper implements RowMapper<Response>  {

    private final List<String> roles;
    public ResponseMapper(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public Response mapRow(ResultSet rs, int rowNum) throws SQLException {
        AppUser appUser =  new AppUser(
                rs.getInt("app_user_id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getBoolean("enabled"),
                roles);

        User user = new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getString("title"),
                rs.getString("university"),
                rs.getString("email"),
                rs.getInt("amount"),
                appUser);

        Subject subject = new Subject(
                rs.getInt("subject_id"),
                rs.getString("name"),
                rs.getString("description")
        );

        Question question = new Question(
                rs.getInt("question_id"),
                rs.getString("question"),
                rs.getString("hint"),
                subject,
                user);

        AnswerOptions answerOptions = new  AnswerOptions(
                rs.getInt("answer_option_id"),
                rs.getString("option"),
                rs.getBoolean("correct"),
                question
        );

        return new Response(
                question,
                user,
                answerOptions
        );
    }
}
