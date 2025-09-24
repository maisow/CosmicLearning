package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AppUser;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AppUserJdbcClientRepository implements AppUserRepository {

    private final JdbcClient jdbcClient;

    public AppUserJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    @Override
    public AppUser findByUsername(String username) {
        List<String> roles = getRolesByUsername(username);

        final String sql = "select app_user_id, username, password_hash, enabled "
                + "from app_user "
                + "where username = ?;";

        return jdbcClient.sql(sql)
                .param(username)
                .query(new AppUserMapper(roles))
                .optional().orElse(null);
    }

    @Override
    public AppUser create(AppUser appUser) {
        final String sql = "insert into app_user (username, password_hash) values (?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param(appUser.getUsername())
                .param(appUser.getPassword())
                .update(keyHolder, "app_user_id");

        if (rowsAffected <= 0) {
            return null;
        }

        appUser.setId(keyHolder.getKey().intValue());

        updateRoles(appUser);

        return appUser;
    }

    @Override
    public boolean update(AppUser appUser) {
        final String sql = "update app_user set "
                + "username = ?, "
                + "enabled = ? "
                + "where app_user_id = ?";

        boolean updated = jdbcClient.sql(sql)
                .param(appUser.getUsername())
                .param(appUser.isEnabled())
                .param(appUser.getId())
                .update() > 0;

        if (updated) {
            updateRoles(appUser);
        }

        return updated;
    }

    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "inner join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.username = ?";
        return jdbcClient.sql(sql)
                .param(username)
                .query((rs, rowId) -> rs.getString("name"))
                .list();
    }

    private void updateRoles(AppUser user) {
        // delete all roles, then re-add
        jdbcClient.sql("delete from app_user_role where app_user_id = ?;")
                .param(user.getId())
                .update();

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (GrantedAuthority role : authorities) {
            String sql = "insert into app_user_role (app_user_id, app_role_id) "
                    + "select ?, app_role_id from app_role where `name` = ?;";
            jdbcClient.sql(sql)
                    .param(user.getId())
                    .param(role.getAuthority())
                    .update();
        }
    }
}
