package com.parttime.service.impl;

import com.parttime.entity.JobApply;
import com.parttime.mapper.JobApplyMapper;
import com.parttime.service.JobApplyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobApplyServiceImpl implements JobApplyService {

    private final JobApplyMapper jobApplyMapper;

    public JobApplyServiceImpl(JobApplyMapper jobApplyMapper) {
        this.jobApplyMapper = jobApplyMapper;
    }

    @Override
    public List<JobApply> findAll() {
        return jobApplyMapper.findAll();
    }

    @Override
    public List<JobApply> findByUserId(Integer userId) {
        return jobApplyMapper.findByUserId(userId);
    }

    @Override
    public int add(JobApply jobApply) {
        jobApply.setApplyTime(LocalDateTime.now());
        jobApply.setStatus("pending");
        return jobApplyMapper.insert(jobApply);
    }

    @Override
    public int updateStatus(JobApply jobApply) {
        return jobApplyMapper.updateStatus(jobApply);
    }

    @Override
    public int delete(Integer id) {
        return jobApplyMapper.deleteById(id);
    }

    @Override
    public boolean hasApplied(Integer userId, Integer jobId) {
        return jobApplyMapper.countByUserAndJob(userId, jobId) > 0;
    }
}
