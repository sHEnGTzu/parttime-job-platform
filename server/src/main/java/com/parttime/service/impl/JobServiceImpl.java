package com.parttime.service.impl;

import com.parttime.entity.Job;
import com.parttime.mapper.JobMapper;
import com.parttime.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    public JobServiceImpl(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    @Override
    public List<Job> findAll() {
        return jobMapper.findAll();
    }

    @Override
    public List<Job> search(String keyword) {
        return jobMapper.search(keyword);
    }

    @Override
    public Job findById(Integer id) {
        return jobMapper.findById(id);
    }

    @Override
    public int add(Job job) {
        return jobMapper.insert(job);
    }

    @Override
    public int update(Job job) {
        return jobMapper.update(job);
    }

    @Override
    public int delete(Integer id) {
        return jobMapper.deleteById(id);
    }
}
