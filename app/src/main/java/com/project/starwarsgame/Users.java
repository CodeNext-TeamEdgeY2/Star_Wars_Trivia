package com.project.starwarsgame;

public class Users {
    String Name;
    String Email;
    String image_url;
    int High_Score;
    int vehicles_score;
    int people_score;
    int species_score;
    int planets_score;
    int starships_score;
    int films_score;
    int numOfFriends;
    boolean exists;

    public Users(String name, String email, String image_url, int high_Score, int vehicles_score, int people_score, int species_score, int planets_score, int starships_score, int films_score, int numOfFriends, boolean exists) {
        Name = name;
        Email = email;
        this.image_url = image_url;
        High_Score = high_Score;
        this.vehicles_score = vehicles_score;
        this.people_score = people_score;
        this.species_score = species_score;
        this.planets_score = planets_score;
        this.starships_score = starships_score;
        this.films_score = films_score;
        this.numOfFriends = numOfFriends;
        this.exists = exists;
    }

    public Users() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getHigh_Score() {
        return High_Score;
    }

    public void setHigh_Score(int high_Score) {
        High_Score = high_Score;
    }

    public int getVehicles_score() {
        return vehicles_score;
    }

    public void setVehicles_score(int vehicles_score) {
        this.vehicles_score = vehicles_score;
    }

    public int getPeople_score() {
        return people_score;
    }

    public void setPeople_score(int people_score) {
        this.people_score = people_score;
    }

    public int getSpecies_score() {
        return species_score;
    }

    public void setSpecies_score(int species_score) {
        this.species_score = species_score;
    }

    public int getPlanets_score() {
        return planets_score;
    }

    public void setPlanets_score(int planets_score) {
        this.planets_score = planets_score;
    }

    public int getStarships_score() {
        return starships_score;
    }

    public void setStarships_score(int starships_score) {
        this.starships_score = starships_score;
    }

    public int getFilms_score() {
        return films_score;
    }

    public void setFilms_score(int films_score) {
        this.films_score = films_score;
    }

    public int getNumOfFriends() {
        return numOfFriends;
    }

    public void setNumOfFriends(int numOfFriends) {
        this.numOfFriends = numOfFriends;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
