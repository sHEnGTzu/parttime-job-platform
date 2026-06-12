package com.parttime.service;

import com.parttime.entity.JobApply;
import java.util.List;

public interface JobApplyService {
    List<JobApply> findAll();
    List<JobApply> findByUserId(Integer userId);
    int add(JobApply jobApply);
    int updateStatus(JobApply jobApply);
    int delete(Integer id);
    boolean hasApplied(Integer userId, Integer jobId);
}
