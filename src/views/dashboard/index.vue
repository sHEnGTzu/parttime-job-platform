<script setup lang="ts">
import { ref, onMounted, onActivated } from 'vue'
import { NNumberAnimation, NTag, NCard, NTable, NEmpty } from 'naive-ui'

import { getJobList, getApplyList } from '@/api/parttime'
import type { Job, JobApply } from '@/api/parttime'

defineOptions({
  name: 'Dashboard',
})

const jobCount = ref(0)
const applyCount = ref(0)
const pendingCount = ref(0)
const recentJobs = ref<Job[]>([])
const pendingApplies = ref<JobApply[]>([])

async function loadData() {
  const [jobRes, applyRes] = await Promise.all([
    getJobList(),
    getApplyList(),
  ])
  if (jobRes.code === 200 && jobRes.data) {
    jobCount.value = jobRes.data.length
    recentJobs.value = jobRes.data.slice(0, 5)
  }
  if (applyRes.code === 200 && applyRes.data) {
    applyCount.value = applyRes.data.length
    pendingCount.value = applyRes.data.filter((a: JobApply) => a.status === 'pending').length
    pendingApplies.value = applyRes.data.filter((a: JobApply) => a.status === 'pending').slice(0, 5)
  }
}

onMounted(() => {
  loadData()
})

onActivated(() => {
  loadData()
})
</script>
<template>
  <div class="flex flex-col gap-y-4">
    <div class="grid grid-cols-1 gap-4 md:grid-cols-3">
      <NCard title="兼职总数" size="small">
        <div class="text-3xl font-bold text-primary">
          <NNumberAnimation :to="jobCount" />
        </div>
        <p class="text-sm text-gray-400 mt-1">平台发布的兼职岗位</p>
      </NCard>
      <NCard title="申请总数" size="small">
        <div class="text-3xl font-bold text-blue-500">
          <NNumberAnimation :to="applyCount" />
        </div>
        <p class="text-sm text-gray-400 mt-1">累计申请记录</p>
      </NCard>
      <NCard title="待审核" size="small">
        <div class="text-3xl font-bold text-orange-500">
          <NNumberAnimation :to="pendingCount" />
        </div>
        <p class="text-sm text-gray-400 mt-1">待处理的申请</p>
      </NCard>
    </div>
    <div class="grid grid-cols-1 gap-4 lg:grid-cols-2">
      <NCard title="最新兼职" size="small">
        <NEmpty v-if="recentJobs.length === 0" description="暂无兼职" />
        <NTable v-else striped size="small">
          <thead><tr><th>名称</th><th>薪资</th><th>地点</th></tr></thead>
          <tbody>
            <tr v-for="job in recentJobs" :key="job.id">
              <td>{{ job.title }}</td>
              <td><NTag size="small" type="success">{{ job.salary }}</NTag></td>
              <td>{{ job.location }}</td>
            </tr>
          </tbody>
        </NTable>
      </NCard>
      <NCard title="待审核申请" size="small">
        <NEmpty v-if="pendingApplies.length === 0" description="暂无待审核申请" />
        <NTable v-else striped size="small">
          <thead><tr><th>兼职</th><th>申请时间</th></tr></thead>
          <tbody>
            <tr v-for="item in pendingApplies" :key="item.id">
              <td>{{ item.jobTitle }}</td>
              <td>{{ item.applyTime?.replace('T', ' ') }}</td>
            </tr>
          </tbody>
        </NTable>
      </NCard>
    </div>
  </div>
</template>