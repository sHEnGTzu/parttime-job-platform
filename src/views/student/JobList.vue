<script setup lang="ts">
import { onMounted, ref } from 'vue'
import {
  NButton,
  NCard,
  NInput,
  NGrid,
  NGridItem,
  NTag,
  NModal,
  NDescriptions,
  NDescriptionsItem,
  NSpace,
  NEmpty,
  useMessage,
} from 'naive-ui'

import { getJobList, addApply, getApplyList } from '@/api/parttime'
import { useUserStore } from '@/stores'

import type { Job, JobApply } from '@/api/parttime'

const userStore = useUserStore()
const message = useMessage()

const jobs = ref<Job[]>([])
const loading = ref(false)
const keyword = ref('')
const showDetail = ref(false)
const currentJob = ref<Job | null>(null)
const appliedJobIds = ref<Set<number>>(new Set())

async function fetchJobs() {
  loading.value = true
  try {
    const res = await getJobList(keyword.value || undefined)
    if (res.code === 200 && res.data) {
      jobs.value = res.data
    }
  } finally {
    loading.value = false
  }
}

async function fetchApplied() {
  if (!userStore.user?.id) return
  const res = await getApplyList(userStore.user.id)
  if (res.code === 200 && res.data) {
    appliedJobIds.value = new Set(res.data.map((a: JobApply) => a.jobId))
  }
}

function viewDetail(job: Job) {
  currentJob.value = job
  showDetail.value = true
}

async function handleApply(jobId: number) {
  try {
    const res = await addApply({ userId: userStore.user!.id, jobId })
    if (res.code === 200) {
      message.success('申请成功！')
      appliedJobIds.value.add(jobId)
    }
  } catch (e: any) {
    message.error(e?.response?.data?.msg || '申请失败')
  }
}

function onSearch() {
  fetchJobs()
}

onMounted(() => {
  fetchJobs()
  fetchApplied()
})
</script>

<template>
  <div>
    <NCard title="兼职信息" :bordered="false">
      <template #header-extra>
        <NSpace>
          <NInput
            v-model:value="keyword"
            placeholder="搜索兼职名称或地点"
            clearable
            style="width: 250px"
            @keyup.enter="onSearch"
          />
          <NButton type="primary" @click="onSearch">搜索</NButton>
        </NSpace>
      </template>

      <div v-if="!loading && jobs.length === 0">
        <NEmpty description="暂无兼职信息" />
      </div>

      <NGrid cols="1 s:2 m:3" :x-gap="16" :y-gap="16">
        <NGridItem v-for="job in jobs" :key="job.id">
          <NCard
            :title="job.title"
            hoverable
            size="small"
            @click="viewDetail(job)"
          >
            <template #header-extra>
              <NTag v-if="appliedJobIds.has(job.id!)" type="success" size="small">已申请</NTag>
            </template>
            <p class="text-sm text-gray-500 mb-1">📍 {{ job.location }}</p>
            <p class="text-sm text-gray-500 mb-1">💰 {{ job.salary }}</p>
            <p class="text-sm text-gray-500">🕐 {{ job.workTime }}</p>
            <template #footer>
              <NButton
                type="primary"
                size="small"
                :disabled="appliedJobIds.has(job.id!)"
                @click.stop="handleApply(job.id!)"
              >
                {{ appliedJobIds.has(job.id!) ? '已申请' : '立即申请' }}
              </NButton>
            </template>
          </NCard>
        </NGridItem>
      </NGrid>
    </NCard>

    <NModal v-model:show="showDetail" title="兼职详情" preset="card" style="width: 600px">
      <NDescriptions v-if="currentJob" bordered :column="1" size="medium">
        <NDescriptionsItem label="兼职名称">{{ currentJob.title }}</NDescriptionsItem>
        <NDescriptionsItem label="工作地点">{{ currentJob.location }}</NDescriptionsItem>
        <NDescriptionsItem label="薪资待遇">{{ currentJob.salary }}</NDescriptionsItem>
        <NDescriptionsItem label="工作时间">{{ currentJob.workTime }}</NDescriptionsItem>
        <NDescriptionsItem label="工作内容">{{ currentJob.description }}</NDescriptionsItem>
      </NDescriptions>
      <template #footer>
        <NSpace justify="center">
          <NButton
            v-if="currentJob"
            type="primary"
            :disabled="appliedJobIds.has(currentJob.id!)"
            @click="handleApply(currentJob.id!)"
          >
            {{ appliedJobIds.has(currentJob.id!) ? '已申请' : '申请该兼职' }}
          </NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>
