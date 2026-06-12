package com.parttime.controller;

import com.parttime.common.Result;
import com.parttime.entity.JobApply;
import com.parttime.service.JobApplyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apply")
public class JobApplyController {

    private final JobApplyService jobApplyService;

    public JobApplyController(JobApplyService jobApplyService) {
        this.jobApplyService = jobApplyService;
    }

    @GetMapping("/list")
    public Result<List<JobApply>> list(@RequestParam(required = false) Integer userId) {
        if (userId != null) {
            return Result.success(jobApplyService.findByUserId(userId));
        }
        return Result.success(jobApplyService.findAll());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody JobApply jobApply) {
        if (jobApplyService.hasApplied(jobApply.getUserId(), jobApply.getJobId())) {
            return Result.error(400, "您已经申请过该兼职");
        }
        jobApplyService.add(jobApply);
        return Result.success();
    }

    @PutMapping("/status")
    public Result<?> updateStatus(@RequestBody JobApply jobApply) {
        jobApplyService.updateStatus(jobApply);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        jobApplyService.delete(id);
        return Result.success();
    }
}
