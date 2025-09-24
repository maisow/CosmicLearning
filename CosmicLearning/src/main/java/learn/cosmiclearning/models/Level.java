package learn.cosmiclearning.models;

import java.util.Objects;

public class Level {

    private int levelId;

    private int level;

    private int amount_points;

    public Level(int levelId, int level, int amount_points) {
        this.amount_points = amount_points;
        this.level = level;
        this.levelId = levelId;
    }

    public int getAmount_points() {
        return amount_points;
    }

    public void setAmount_points(int amount_points) {
        this.amount_points = amount_points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Level level1)) return false;
        return getLevelId() == level1.getLevelId() && getLevel() == level1.getLevel() && getAmount_points() == level1.getAmount_points();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLevelId(), getLevel(), getAmount_points());
    }

    @Override
    public String toString() {
        return "Level{" +
                "amount_points=" + amount_points +
                ", levelId=" + levelId +
                ", level=" + level +
                '}';
    }
}
