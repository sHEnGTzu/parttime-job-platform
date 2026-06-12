<script setup lang="ts">
import { onMounted, ref } from 'vue'
import {
  NButton,
  NCard,
  NInput,
  NTag,
  NModal,
  NForm,
  NFormItem,
  NTable,
  NEmpty,
  NSpace,
  useMessage,
  NPopconfirm,
} from 'naive-ui'

import { getJobList, addJob, updateJob, deleteJob } from '@/api/parttime'

import type { Job } from '@/api/parttime'

const message = useMessage()

const jobs = ref<Job[]>([])
const loading = ref(false)
const showModal = ref(false)
const editing = ref(false)
const keyword = ref('')

const form = ref<Job>({
  title: '',
  location: '',
  salary: '',
  workTime: '',
  description: '',
})

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

function openAdd() {
  editing.value = false
  form.value = { title: '', location: '', salary: '', workTime: '', description: '' }
  showModal.value = true
}

function openEdit(job: Job) {
  editing.value = true
  form.value = { ...job }
  showModal.value = true
}

async function handleSave() {
  if (!form.value.title) {
    message.error('请输入兼职名称')
    return
  }
  try {
    if (editing.value) {
      await updateJob(form.value)
      message.success('修改成功')
    } else {
      await addJob(form.value)
      message.success('添加成功')
    }
    showModal.value = false
    fetchJobs()
  } catch {
    message.error('操作失败')
  }
}

async function handleDelete(id: number) {
  try {
    await deleteJob(id)
    message.success('删除成功')
    fetchJobs()
  } catch {
    message.error('删除失败')
  }
}

function onSearch() {
  fetchJobs()
}

onMounted(fetchJobs)
</script>

<template>
  <div>
    <NCard title="兼职管理" :bordered="false">
      <template #header-extra>
        <NSpace>
          <NInput
            v-model:value="keyword"
            placeholder="搜索兼职名称"
            clearable
            style="width: 200px"
            @keyup.enter="onSearch"
          />
          <NButton @click="onSearch">搜索</NButton>
          <NButton type="primary" @click="openAdd">发布兼职</NButton>
        </NSpace>
      </template>

      <NEmpty v-if="!loading && jobs.length === 0" description="暂无兼职信息" />

      <NTable v-else striped :bordered="false" size="small">
        <thead>
          <tr>
            <th>编号</th>
            <th>兼职名称</th>
            <th>工作地点</th>
            <th>薪资</th>
            <th>工作时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(job, idx) in jobs" :key="job.id">
            <td>{{ idx + 1 }}</td>
            <td>{{ job.title }}</td>
            <td>{{ job.location }}</td>
            <td><NTag type="success">{{ job.salary }}</NTag></td>
            <td>{{ job.workTime }}</td>
            <td>
              <NSpace>
                <NButton size="tiny" type="primary" ghost @click="openEdit(job)">编辑</NButton>
                <NPopconfirm @positive-click="handleDelete(job.id!)">
                  <template #trigger>
                    <NButton size="tiny" type="error" ghost>删除</NButton>
                  </template>
                  确定删除该兼职吗？
                </NPopconfirm>
              </NSpace>
            </td>
          </tr>
        </tbody>
      </NTable>
    </NCard>

    <NModal
      v-model:show="showModal"
      :title="editing ? '编辑兼职' : '发布兼职'"
      preset="card"
      style="width: 600px"
    >
      <NForm label-placement="left" label-width="100px">
        <NFormItem label="兼职名称">
          <NInput v-model:value="form.title" placeholder="请输入兼职名称" />
        </NFormItem>
        <NFormItem label="工作地点">
          <NInput v-model:value="form.location" placeholder="请输入工作地点" />
        </NFormItem>
        <NFormItem label="薪资待遇">
          <NInput v-model:value="form.salary" placeholder="请输入薪资待遇" />
        </NFormItem>
        <NFormItem label="工作时间">
          <NInput v-model:value="form.workTime" placeholder="请输入工作时间" />
        </NFormItem>
        <NFormItem label="工作内容">
          <NInput
            v-model:value="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入工作内容"
          />
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace justify="center">
          <NButton @click="showModal = false">取消</NButton>
          <NButton type="primary" @click="handleSave">保存</NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>
