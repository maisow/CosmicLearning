package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Level;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class LevelJdbcClientRepository implements LevelRepository {

    private final JdbcClient jdbcClient;

    public LevelJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Level createLevel(Level level) {

        final String sql = "insert into level (level, amount_points) " +
                "values (:level, :amount_points);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("level", level.getLevel())
                .param("amount_points", level.getAmount_points())
                .update(keyHolder, "level_id");

        if (rowsAffected <= 0) {
            return null;
        }

        level.setLevelId(keyHolder.getKey().intValue());

        return level;
    }

    @Override
    public boolean updateLevel(Level level) {
        final String sql = "update level set "
                + "level = :level, "
                + "amount_points = :amount_points, "
                + "where level_id = :level_id;";

        return jdbcClient.sql(sql)
                .param("level", level.getLevel())
                .param("amount_points", level.getAmount_points())
                .param("level_id", level.getLevelId())
                .update() > 0;
    }

    @Override
    public boolean deleteLevel(int levelId) {
        return jdbcClient.sql("delete from level where level_id = ?;").param(levelId).update() > 0;
    }

    @Override
    public Level getLevelByPoints(int amountPoints) {
        String sql = """
        select
            level_id,
            level, 
            amount_points
        from level 
        where amount_points = ?;
        """;

        return jdbcClient.sql(sql)
                .param(amountPoints)
                .query(new LevelMapper())
                .optional()
                .orElse(null);
    }
}
