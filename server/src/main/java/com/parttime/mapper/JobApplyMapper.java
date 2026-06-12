package com.parttime.mapper;

import com.parttime.entity.JobApply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobApplyMapper {
    @Select("SELECT * FROM job_apply WHERE id = #{id}")
    JobApply findById(Integer id);

    @Select("SELECT ja.*, j.title AS jobTitle, j.location AS jobLocation, j.salary AS jobSalary FROM job_apply ja LEFT JOIN job j ON ja.job_id = j.id WHERE ja.user_id = #{userId} ORDER BY ja.apply_time DESC")
    List<JobApply> findByUserId(Integer userId);

    @Select("SELECT ja.*, j.title AS jobTitle, j.location AS jobLocation, j.salary AS jobSalary FROM job_apply ja LEFT JOIN job j ON ja.job_id = j.id ORDER BY ja.apply_time DESC")
    List<JobApply> findAll();

    @Select("SELECT COUNT(*) FROM job_apply WHERE user_id = #{userId} AND job_id = #{jobId}")
    int countByUserAndJob(@Param("userId") Integer userId, @Param("jobId") Integer jobId);

    @Insert("INSERT INTO job_apply(user_id, job_id, apply_time, status) VALUES(#{userId}, #{jobId}, #{applyTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(JobApply jobApply);

    @Update("UPDATE job_apply SET status = #{status} WHERE id = #{id}")
    int updateStatus(JobApply jobApply);

    @Delete("DELETE FROM job_apply WHERE id = #{id}")
    int deleteById(Integer id);
}
