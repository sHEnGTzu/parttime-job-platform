package com.parttime.service;

import com.parttime.entity.Job;
import java.util.List;

public interface JobService {
    List<Job> findAll();
    List<Job> search(String keyword);
    Job findById(Integer id);
    int add(Job job);
    int update(Job job);
    int delete(Integer id);
}
