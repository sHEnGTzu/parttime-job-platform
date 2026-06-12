<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { NButton, NCard, NTag, NTable, NEmpty, NSpace, NPopconfirm, useMessage } from 'naive-ui'

import { getApplyList, updateApplyStatus } from '@/api/parttime'

import type { JobApply } from '@/api/parttime'

const message = useMessage()

const applies = ref<JobApply[]>([])
const loading = ref(false)

const statusMap: Record<string, { type: 'info' | 'success' | 'error' | 'warning'; text: string }> = {
  pending: { type: 'warning', text: '待审核' },
  approved: { type: 'success', text: '已通过' },
  rejected: { type: 'error', text: '已拒绝' },
}

async function fetchApplies() {
  loading.value = true
  try {
    const res = await getApplyList()
    if (res.code === 200 && res.data) {
      applies.value = res.data
    }
  } finally {
    loading.value = false
  }
}

async function handleStatus(id: number, status: string) {
  try {
    await updateApplyStatus({ id, status })
    message.success('操作成功')
    fetchApplies()
  } catch {
    message.error('操作失败')
  }
}

onMounted(fetchApplies)
</script>

<template>
  <div>
    <NCard title="申请管理" :bordered="false">
      <NEmpty v-if="!loading && applies.length === 0" description="暂无申请记录" />

      <NTable v-else striped :bordered="false" size="small">
        <thead>
          <tr>
            <th>编号</th>
            <th>兼职名称</th>
            <th>工作地点</th>
            <th>薪资</th>
            <th>申请时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, idx) in applies" :key="item.id">
            <td>{{ idx + 1 }}</td>
            <td>{{ item.jobTitle }}</td>
            <td>{{ item.jobLocation }}</td>
            <td>{{ item.jobSalary }}</td>
            <td>{{ item.applyTime?.replace('T', ' ') }}</td>
            <td>
              <NTag :type="statusMap[item.status]?.type || 'default'">
                {{ statusMap[item.status]?.text || item.status }}
              </NTag>
            </td>
            <td>
              <NSpace v-if="item.status === 'pending'">
                <NButton size="tiny" type="success" @click="handleStatus(item.id, 'approved')">
                  通过
                </NButton>
                <NButton size="tiny" type="error" @click="handleStatus(item.id, 'rejected')">
                  拒绝
                </NButton>
              </NSpace>
              <span v-else class="text-gray-400 text-sm">已处理</span>
            </td>
          </tr>
        </tbody>
      </NTable>
    </NCard>
  </div>
</template>
