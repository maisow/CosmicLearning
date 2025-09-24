package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Level;

public interface LevelRepository {

    Level createLevel(Level level);

    boolean updateLevel(Level level);

    boolean deleteLevel(int levelId);

    Level getLevelByPoints(int amountPoints);

}
