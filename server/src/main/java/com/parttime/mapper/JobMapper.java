package com.parttime.mapper;

import com.parttime.entity.Job;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobMapper {
    @Select("SELECT * FROM job WHERE id = #{id}")
    Job findById(Integer id);

    @Select("SELECT * FROM job ORDER BY id DESC")
    List<Job> findAll();

    @Select("SELECT * FROM job WHERE title LIKE CONCAT('%', #{keyword}, '%') OR location LIKE CONCAT('%', #{keyword}, '%') ORDER BY id DESC")
    List<Job> search(@Param("keyword") String keyword);

    @Insert("INSERT INTO job(title, location, salary, work_time, description) VALUES(#{title}, #{location}, #{salary}, #{workTime}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Job job);

    @Update("UPDATE job SET title = #{title}, location = #{location}, salary = #{salary}, work_time = #{workTime}, description = #{description} WHERE id = #{id}")
    int update(Job job);

    @Delete("DELETE FROM job WHERE id = #{id}")
    int deleteById(Integer id);
}
