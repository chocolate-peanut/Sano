package com.example.sano;

public class GoalModel {

    private String Goal;
    private String GoalCheckbox;

    public GoalModel(){};

    public GoalModel(String Goal, String GoalCheckbox){
        this.Goal = Goal;
        this.GoalCheckbox = GoalCheckbox;
    }

    public String getGoal(){return Goal;}

    public void setGoal(String Goal){this.Goal = Goal;}

    public String getGoalCheckbox(){return GoalCheckbox;}

    public void setGoalCheckbox(String GoalCheckbox){this.GoalCheckbox = GoalCheckbox;}
}