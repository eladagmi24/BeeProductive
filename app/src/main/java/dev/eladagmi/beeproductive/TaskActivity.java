package dev.eladagmi.beeproductive;

public class TaskActivity {

    private String taskName, creatorName, members, dueDate, hourToPerform, importance;
    private double lat=0.0;
    private double lon=0.0;


    public TaskActivity() {
    }

    public String getMembers() {
        return members;
    }

    public TaskActivity setMembers(String members) {
        this.members = members;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskActivity setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public TaskActivity setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public String getDueDate() {
        return dueDate;
    }

    public TaskActivity setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getHourToPerform() {
        return hourToPerform;
    }

    public TaskActivity setHourToPerform(String hourToPerform) {
        this.hourToPerform = hourToPerform;
        return this;
    }

    public String getImportance() {
        return importance;
    }

    public TaskActivity setImportance(String importance) {
        this.importance = importance;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public TaskActivity setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public TaskActivity setLon(double lon) {
        this.lon = lon;
        return this;
    }
    @Override
    public String toString() {
        return "TaskName: " + taskName + "| Hour to perform:" + hourToPerform;
    }
}