package com.ubs.m295_projectapplication.jdbc;

import com.ubs.module.Project;
import com.ubs.module.Team;
import com.ubs.module.TeamMember;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TeamDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public TeamDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private List<TeamMember> teamMemberList = new ArrayList<>();

    public List<Team> getAllTeams() {
        String sql = "select * from team t join software s on t.teamId = s.team join project p on s.project = p.projectId join teammember tm on t.teamId = tm.team";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Team.class));
    }


    public Team getTeamById(int teamId) {
        String sql = "select * from team t join software s on t.teamId = s.team join project p on s.project = p.projectId join teammember tm on t.teamId = tm.team WHERE teamId = :teamId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("teamId", teamId);
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new BeanPropertyRowMapper<>(Team.class));
    }

    public void addTeam(Team team) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String sql = "insert into team (teamName, budget) values (:teamName, :budget)";
        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("teamName", team.getTeamName())
                .addValue("budget", team.getBudget());
        int status = namedParameterJdbcTemplate.update(sql, paramSource, generatedKeyHolder);
        int id = generatedKeyHolder.getKey().intValue();
        System.out.println(id);
        team.setTeamId((long) id);
    }

    public int updateTeam(Team team) {
        String sql = "update team set teamName = :teamName, budget = :budget where teamId = :teamId";
        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("teamName", team.getTeamName())
                .addValue("budget", team.getBudget())
                .addValue("teamId", team.getTeamId());
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public int deleteTeamById(int teamId) {
        String sql = "delete from team where teamId = :teamId";
        SqlParameterSource paramSource = new MapSqlParameterSource("teamId", teamId);
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }


}
