package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AppUser;
import learn.cosmiclearning.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserMapper implements RowMapper<User> {
    private final List<String> roles;

    public UserMapper(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getString("title"),
                rs.getString("university"),
                rs.getInt("amount"),
                rs.getInt("app_user_id")
        );
    }
}
