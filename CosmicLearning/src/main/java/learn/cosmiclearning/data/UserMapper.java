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

        AppUser appUser =  new AppUser(
                rs.getInt("app_user_id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getBoolean("enabled"),
                roles);

        return new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getString("title"),
                rs.getString("university"),
                rs.getString("email"),
                rs.getInt("amount"),
                appUser
        );
    }
}
