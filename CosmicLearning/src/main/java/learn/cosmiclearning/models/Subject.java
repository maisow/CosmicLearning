package learn.cosmiclearning.models;

import java.util.Objects;

public class Subject {

    private int subjectId;
    private String name;
    private String description;

    public Subject(int subjectId, String name, String description) {
        this.description = description;
        this.name = name;
        this.subjectId = subjectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Subject subject)) return false;
        return getSubjectId() == subject.getSubjectId() && Objects.equals(getName(), subject.getName()) && Objects.equals(getDescription(), subject.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubjectId(), getName(), getDescription());
    }

    @Override
    public String toString() {
        return "Subject{" +
                "description='" + description + '\'' +
                ", subjectId=" + subjectId +
                ", name='" + name + '\'' +
                '}';
    }
}
