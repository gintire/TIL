package com.gintire.springboottest.domain;

import java.util.List;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.domain
 * <p>
 * User: jin36
 * Date: 2021-01-12
 * Time: 오후 9:50
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Player {

    private String name;
    private String team;
    private List<String> teamMates;

    public Player(String name, String team, List<String> teamMates) {
        this.name = name;
        this.team = team;
        this.teamMates = teamMates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setTeamMates(List<String> teamMates) {
        this.teamMates = teamMates;
    }

    // constructor and setters omitted
    public String getName() {
        return name;
    }
    public String getTeam() {
        return team;
    }
    public List<String> getTeamMates() {
        return teamMates;
    }

    @Override
    public String toString() {
        return "Player[name="+name+"]";
    }
}
