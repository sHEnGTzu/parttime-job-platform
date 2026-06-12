<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { NButton, NCard, NTag, NTable, NEmpty, NModal, NDescriptions, NDescriptionsItem, useMessage, NSpace } from 'naive-ui'

import { getApplyList, deleteApply, getJobDetail } from '@/api/parttime'
import { useUserStore } from '@/stores'

import type { JobApply, Job } from '@/api/parttime'

const userStore = useUserStore()
const message = useMessage()

const applies = ref<JobApply[]>([])
const loading = ref(false)
const showDetail = ref(false)
const currentJob = ref<Job | null>(null)

const statusMap: Record<string, { type: 'info' | 'success' | 'error' | 'warning'; text: string }> = {
  pending: { type: 'warning', text: '待审核' },
  approved: { type: 'success', text: '已通过' },
  rejected: { type: 'error', text: '已拒绝' },
}

async function fetchApplies() {
  loading.value = true
  try {
    const res = await getApplyList(userStore.user!.id)
    if (res.code === 200 && res.data) {
      applies.value = res.data
    }
  } finally {
    loading.value = false
  }
}

function handleCancel(id: number) {
  deleteApply(id).then(() => {
    message.success('已取消申请')
    fetchApplies()
  }).catch(() => {
    message.error('操作失败')
  })
}

async function viewJobDetail(jobId: number) {
  const res = await getJobDetail(jobId)
  if (res.code === 200 && res.data) {
    currentJob.value = res.data
    showDetail.value = true
  }
}

onMounted(fetchApplies)
</script>

<template>
  <div>
    <NCard title="我的申请记录" :bordered="false">
      <NEmpty v-if="!loading && applies.length === 0" description="暂无申请记录" />

      <NTable v-else striped :bordered="false" size="small">
        <thead>
          <tr>
            <th>兼职名称</th>
            <th>工作地点</th>
            <th>薪资</th>
            <th>申请时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in applies" :key="item.id">
            <td>
              <NButton text type="primary" @click="viewJobDetail(item.jobId)">
                {{ item.jobTitle }}
              </NButton>
            </td>
            <td>{{ item.jobLocation }}</td>
            <td>{{ item.jobSalary }}</td>
            <td>{{ item.applyTime?.replace('T', ' ') }}</td>
            <td>
              <NTag :type="statusMap[item.status]?.type || 'default'">
                {{ statusMap[item.status]?.text || item.status }}
              </NTag>
            </td>
            <td>
              <NButton
                v-if="item.status === 'pending'"
                size="tiny"
                type="error"
                @click="handleCancel(item.id)"
              >
                取消申请
              </NButton>
            </td>
          </tr>
        </tbody>
      </NTable>
    </NCard>

    <NModal v-model:show="showDetail" title="兼职详情" preset="card" style="width: 600px">
      <NDescriptions v-if="currentJob" bordered :column="1" size="medium">
        <NDescriptionsItem label="兼职名称">{{ currentJob.title }}</NDescriptionsItem>
        <NDescriptionsItem label="工作地点">{{ currentJob.location }}</NDescriptionsItem>
        <NDescriptionsItem label="薪资待遇">{{ currentJob.salary }}</NDescriptionsItem>
        <NDescriptionsItem label="工作时间">{{ currentJob.workTime }}</NDescriptionsItem>
        <NDescriptionsItem label="工作内容">{{ currentJob.description }}</NDescriptionsItem>
      </NDescriptions>
    </NModal>
  </div>
</template>
