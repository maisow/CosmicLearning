package learn.cosmiclearning.data;

import learn.cosmiclearning.models.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserJdbcClientRepository implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public User createProfile(User user) {
        final String sql = "insert into user (first_name, last_name, age, title, university," +
                " email, amount, app_user_id) "
                + "values (:first_name, :last_name, :age, :title, :university, :email, :amount, :app_user_id);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("first_name", user.getFirstName())
                .param("last_name", user.getLastName())
                .param("age", user.getAge())
                .param("title", user.getTitle())
                .param("university", user.getUniversity())
                .param("email", user.getEmail())
                .param("amount", user.getAmount())
                .param("app_user_id", user.getAppUser().getId())
                .update(keyHolder, "user_id");

        if (rowsAffected <= 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public User findProfileById(String userId, String username) {
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
            ap.enabled
        from user u
        join app_user ap on u.app_user_id = ap.app_user_id
        where u.user_id = ?;
        """;

        return jdbcClient.sql(sql)
                .param(userId)
                .query(new UserMapper(roles))
                .optional()
                .orElse(null);
    }


    @Override
    public boolean updateProfile(User user) {
        final String sql = "update user set "
                + "first_name = :first_name, "
                + "last_name = :last_name, "
                + "age = :age, "
                + "title = :title, "
                + "university = :university, "
                + "email = :email, "
                + "amount = :amount, "
                + "app_user_id = :app_user_id "
                + "where user_id = :user_id;";


        return jdbcClient.sql(sql)
                .param("first_name", user.getFirstName())
                .param("last_name", user.getLastName())
                .param("age", user.getAge())
                .param("title", user.getTitle())
                .param("university", user.getUniversity())
                .param("email", user.getEmail())
                .param("amount", user.getAmount())
                .param("app_user_id", user.getAppUser().getId())
                .param("user_id", user.getUserId())
                .update() > 0;
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
