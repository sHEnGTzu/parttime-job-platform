<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { NCard, NForm, NFormItem, NInput, NButton, NTag, NSpace, NDivider, useMessage } from 'naive-ui'

import { updateUser, getUserById } from '@/api/parttime'
import { useUserStore } from '@/stores'

import type { FormItemRule } from 'naive-ui'

defineOptions({
  name: 'Profile',
})

const message = useMessage()
const userStore = useUserStore()

const loading = ref(false)
const editing = ref(false)

const form = reactive({
  name: '',
  phone: '',
})

const rules: Record<string, FormItemRule[]> = {
  name: [{ required: true, message: '请输入姓名', trigger: ['input'] }],
  phone: [{ required: true, message: '请输入联系方式', trigger: ['input'] }],
}

function loadUser() {
  if (userStore.user) {
    form.name = userStore.user.name
    form.phone = userStore.user.phone
  }
}

function startEdit() {
  editing.value = true
}

function cancelEdit() {
  loadUser()
  editing.value = false
}

async function save() {
  if (!form.name || !form.phone) {
    message.error('请填写完整信息')
    return
  }
  loading.value = true
  try {
    const res = await updateUser({ id: userStore.user!.id, name: form.name, phone: form.phone })
    if (res.code === 200 && res.data) {
      message.success('修改成功')
      userStore.setUser(res.data)
      editing.value = false
    } else {
      message.error(res.msg || '修改失败')
    }
  } catch {
    message.error('修改失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadUser)
</script>

<template>
  <div class="max-w-2xl mx-auto">
    <NCard title="个人中心" :bordered="false">
      <template #header-extra>
        <NSpace>
          <NButton v-if="!editing" type="primary" size="small" @click="startEdit">修改信息</NButton>
        </NSpace>
      </template>

      <div class="flex items-center gap-4 mb-6 pb-6 border-b border-neutral-200 dark:border-neutral-700">
        <div class="w-16 h-16 rounded-full bg-primary/10 flex items-center justify-center text-2xl font-bold text-primary">
          {{ userStore.user?.name?.charAt(0) || '?' }}
        </div>
        <div>
          <div class="text-lg font-semibold">{{ userStore.user?.name }}</div>
          <div class="text-sm text-gray-400">{{ userStore.user?.username }}</div>
          <NTag :type="userStore.user?.role === 'admin' ? 'warning' : 'info'" size="small" class="mt-1">
            {{ userStore.user?.role === 'admin' ? '管理员' : '学生' }}
          </NTag>
        </div>
      </div>

      <NDivider />
      <div class="text-base font-medium mb-4">基本信息</div>

      <NForm v-if="editing" :model="form" :rules="rules" label-placement="left" label-width="100px">
        <NFormItem label="用户名">
          <NInput :value="userStore.user?.username" disabled />
        </NFormItem>
        <NFormItem label="姓名" path="name">
          <NInput v-model:value="form.name" placeholder="请输入姓名" />
        </NFormItem>
        <NFormItem label="联系方式" path="phone">
          <NInput v-model:value="form.phone" placeholder="请输入联系方式" />
        </NFormItem>
        <NFormItem label="角色">
          <NInput :value="userStore.user?.role === 'admin' ? '管理员' : '学生'" disabled />
        </NFormItem>
        <NSpace justify="center" class="mt-4">
          <NButton @click="cancelEdit">取消</NButton>
          <NButton type="primary" :loading="loading" @click="save">保存</NButton>
        </NSpace>
      </NForm>

      <div v-else class="space-y-4">
        <div class="flex">
          <span class="w-24 text-gray-400 shrink-0">用户名</span>
          <span>{{ userStore.user?.username }}</span>
        </div>
        <div class="flex">
          <span class="w-24 text-gray-400 shrink-0">姓名</span>
          <span>{{ userStore.user?.name }}</span>
        </div>
        <div class="flex">
          <span class="w-24 text-gray-400 shrink-0">联系方式</span>
          <span>{{ userStore.user?.phone || '未设置' }}</span>
        </div>
        <div class="flex">
          <span class="w-24 text-gray-400 shrink-0">角色</span>
          <NTag :type="userStore.user?.role === 'admin' ? 'warning' : 'info'" size="small">
            {{ userStore.user?.role === 'admin' ? '管理员' : '学生' }}
          </NTag>
        </div>
      </div>
    </NCard>
  </div>
</template>
