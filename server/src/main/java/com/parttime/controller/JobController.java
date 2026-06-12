package com.parttime.controller;

import com.parttime.common.Result;
import com.parttime.entity.Job;
import com.parttime.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/list")
    public Result<List<Job>> list(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return Result.success(jobService.search(keyword));
        }
        return Result.success(jobService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Job> detail(@PathVariable Integer id) {
        return Result.success(jobService.findById(id));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Job job) {
        jobService.add(job);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Job job) {
        jobService.update(job);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        jobService.delete(id);
        return Result.success();
    }
}
