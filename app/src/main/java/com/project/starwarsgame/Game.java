package com.project.starwarsgame;

public class Game {

    boolean is_game_finish;

    String user0;
    String user1;

    String turn;
    String round1Winner;
    String round2Winner;
    String round3Winner;

    int round;

    int user0round1score;
    int user1round1score;

    int user0round2score;
    int user1round2score;

    int user0round3score;
    int user1round3score;

    public Game(boolean is_game_finish, String user0, String user1, String turn, String round1Winner, String round2Winner, String round3Winner, int round, int user0round1score, int user1round1score, int user0round2score, int user1round2score, int user0round3score, int user1round3score) {
        this.is_game_finish = is_game_finish;
        this.user0 = user0;
        this.user1 = user1;
        this.turn = turn;
        this.round1Winner = round1Winner;
        this.round2Winner = round2Winner;
        this.round3Winner = round3Winner;
        this.round = round;
        this.user0round1score = user0round1score;
        this.user1round1score = user1round1score;
        this.user0round2score = user0round2score;
        this.user1round2score = user1round2score;
        this.user0round3score = user0round3score;
        this.user1round3score = user1round3score;
    }

    public Game() {}

    public boolean isIs_game_finish() {
        return is_game_finish;
    }

    public void setIs_game_finish(boolean is_game_finish) {
        this.is_game_finish = is_game_finish;
    }

    public String getUser0() {
        return user0;
    }

    public void setUser0(String user0) {
        this.user0 = user0;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getRound1Winner() {
        return round1Winner;
    }

    public void setRound1Winner(String round1Winner) {
        this.round1Winner = round1Winner;
    }

    public String getRound2Winner() {
        return round2Winner;
    }

    public void setRound2Winner(String round2Winner) {
        this.round2Winner = round2Winner;
    }

    public String getRound3Winner() {
        return round3Winner;
    }

    public void setRound3Winner(String round3Winner) {
        this.round3Winner = round3Winner;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getUser0round1score() {
        return user0round1score;
    }

    public void setUser0round1score(int user0round1score) {
        this.user0round1score = user0round1score;
    }

    public int getUser1round1score() {
        return user1round1score;
    }

    public void setUser1round1score(int user1round1score) {
        this.user1round1score = user1round1score;
    }

    public int getUser0round2score() {
        return user0round2score;
    }

    public void setUser0round2score(int user0round2score) {
        this.user0round2score = user0round2score;
    }

    public int getUser1round2score() {
        return user1round2score;
    }

    public void setUser1round2score(int user1round2score) {
        this.user1round2score = user1round2score;
    }

    public int getUser0round3score() {
        return user0round3score;
    }

    public void setUser0round3score(int user0round3score) {
        this.user0round3score = user0round3score;
    }

    public int getUser1round3score() {
        return user1round3score;
    }

    public void setUser1round3score(int user1round3score) {
        this.user1round3score = user1round3score;
    }
}