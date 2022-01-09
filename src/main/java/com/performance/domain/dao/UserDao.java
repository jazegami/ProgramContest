package com.performance.domain.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.performance.domain.entity.UserHobby;
import com.performance.domain.entity.UserInfo;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;
    
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    @Transactional
    public Long insertUserInfo (UserInfo entity) {
        String sql = "INSERT INTO user_info (last_name, first_name, prefectures, city, blood_type)";
        sql = sql + " VALUES (";
        sql = sql + "'" + entity.getLastName() + "', ";
        sql = sql + "'" + entity.getFirstName() + "', ";
        sql = sql + "'" + entity.getPrefectures() + "', ";
        sql = sql + "'" + entity.getCity() + "', ";
        sql = sql + "'" + entity.getBloodType() + "') ";
        // 主キーを取得できるように修正
        sql = sql + "RETURNING id";
        //jdbcTemplate.execute(sql);
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    */
    
    public void insertUserInfoAll (List<UserInfo> entity) {
        String sql = "INSERT INTO user_info (last_name, first_name, prefectures, city, blood_type)";
        sql = sql + " VALUES (?, ?, ?, ?, ?)";
        
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
        	
        	@Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
        		UserInfo info = entity.get(i);
                ps.setString(1, info.getLastName());
                ps.setString(2, info.getFirstName());
                ps.setString(3, info.getPrefectures());
                ps.setString(4, info.getCity());
                ps.setString(5, info.getBloodType());
            }
        	
        	@Override
            public int getBatchSize() {
                return entity.size();
            }
        });
    }
    
    @Transactional
    public void insertUserHobby (UserHobby entity) {
        String sql = "INSERT INTO user_hobby (id, hobby1, hobby2, hobby3, hobby4, hobby5)";
        sql = sql + " VALUES (";
        sql = sql + "'" + entity.getId() + "', ";
        sql = sql + "'" + entity.getHobby1() + "', ";
        sql = sql + "'" + entity.getHobby2() + "', ";
        sql = sql + "'" + entity.getHobby3() + "', ";
        sql = sql + "'" + entity.getHobby4() + "', ";
        sql = sql + "'" + entity.getHobby5() + "')";
        jdbcTemplate.execute(sql);
    }
    
    public void insertUserHobbyAll (List<Long> idList ,List<UserHobby> entity) {
        String sql = "INSERT INTO user_hobby (id, hobby1, hobby2, hobby3, hobby4, hobby5)";
        sql = sql + " VALUES (?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
        	
        	@Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
        		UserHobby hobby = entity.get(i);
                ps.setLong(1, idList.get(i));
                ps.setString(2, hobby.getHobby1());
                ps.setString(3, hobby.getHobby2());
                ps.setString(4, hobby.getHobby3());
                ps.setString(5, hobby.getHobby4());
                ps.setString(6, hobby.getHobby5());
            }
        	
        	@Override
            public int getBatchSize() {
                return entity.size();
            }
        });
    }
    
    public Long selectId(UserInfo entity) {
        String sql = "SELECT id ";
        sql = sql + "FROM user_info ";
        sql = sql + "WHERE last_name || first_name = " + "'" + entity.getLastName() + entity.getFirstName() + "'";
        sql = sql + " ORDER BY id desc";
        sql = sql + " LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    public List<Long> selectIdList() {
        String sql = "SELECT id ";
        sql = sql + "FROM user_info ";
        sql = sql + " ORDER BY id";
        return jdbcTemplate.queryForObject(sql, List.class);
    }

    public List<UserInfo> searchUserInfo() {
        String sql = "SELECT id, last_name, first_name, prefectures, city, blood_type ";
        sql = sql + "FROM user_info ";
        sql = sql + "WHERE last_name || first_name <> " + "'試験太郎'";
        sql = sql + " ORDER BY id";
        RowMapper<UserInfo> mapper = new BeanPropertyRowMapper<UserInfo>(UserInfo.class);
        return jdbcTemplate.query(sql, mapper);
    }

    public List<UserHobby> searchUserHobby(UserHobby targetUserHobby) {
        String sql = "SELECT id, hobby1, hobby2, hobby3, hobby4, hobby5 ";
        sql = sql + "FROM user_hobby ";
        sql = sql + "WHERE id  <> " + targetUserHobby.getId();
        sql = sql + " ORDER BY id";
        RowMapper<UserHobby> mapper = new BeanPropertyRowMapper<UserHobby>(UserHobby.class);
        return jdbcTemplate.query(sql, mapper);
    }
    
    public UserInfo getTargetUserInfo() {
        String sql = "SELECT id, last_name, first_name, prefectures, city, blood_type ";
        sql = sql + "FROM user_info ";
        sql = sql + "WHERE last_name = " + "'試験'";
        sql = sql + "AND first_name = " + "'太郎'";
        RowMapper<UserInfo> mapper = new BeanPropertyRowMapper<UserInfo>(UserInfo.class);
        return jdbcTemplate.queryForObject(sql, mapper);
    }
    
    public UserHobby getTargetUserHobby(UserInfo userInfo) {
        String sql = "SELECT id, hobby1, hobby2, hobby3, hobby4, hobby5 ";
        sql = sql + "FROM user_hobby ";
        sql = sql + "WHERE id = " + userInfo.getId();
        RowMapper<UserHobby> mapper = new BeanPropertyRowMapper<UserHobby>(UserHobby.class);
        return jdbcTemplate.queryForObject(sql, mapper);
    }
    
    public int searchCount() {
        String sql = "SELECT COUNT(*) FROM user_info";
        
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void truncateUserInfo() {
        String sql = "TRUNCATE TABLE user_info";
        jdbcTemplate.execute(sql);
    }
    
    public void truncateUserHobby() {
        String sql = "TRUNCATE TABLE user_hobby";
        jdbcTemplate.execute(sql);
    }
    
}
