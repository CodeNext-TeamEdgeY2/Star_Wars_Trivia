package com.project.starwarsgame;

public class leaderboardUser {
    String Name;
    int High_Score;

    public leaderboardUser(String name, int high_Score) {
        Name = name;
        High_Score = high_Score;
    }

    public leaderboardUser() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getHigh_Score() {
        return High_Score;
    }

    public void setHigh_Score(int high_Score) {
        High_Score = high_Score;
    }
}
