<script setup lang="ts">
import {
  NForm,
  NFormItem,
  NInput,
  NButton,
  NCarousel,
  NTab,
  NTabs,
  useMessage,
} from 'naive-ui'
import {
  computed,
  defineAsyncComponent,
  onMounted,
  onUnmounted,
  reactive,
  ref,
  useTemplateRef,
} from 'vue'

import topographySvg from '@/assets/topography.svg'
import { useInjection } from '@/composables'
import { mediaQueryInjectionKey } from '@/injection'
import ThemeModePopover from '@/layout/header/action/ThemeModePopover.vue'
import router from '@/router'
import { toRefsPreferencesStore, useUserStore } from '@/stores'
import { twColor } from '@/utils/colors'

import ThemeColorPopover from './component/ThemeColorPopover.vue'

import type { FormItemRule } from 'naive-ui'

defineOptions({
  name: 'SignIn',
})

const message = useMessage()
const { isMaxSm } = useInjection(mediaQueryInjectionKey)
const { isDark } = toRefsPreferencesStore()
const userStore = useUserStore()

const illustrations = [
  defineAsyncComponent(() => import('./component/Illustration1.vue')),
  defineAsyncComponent(() => import('./component/Illustration2.vue')),
  defineAsyncComponent(() => import('./component/Illustration3.vue')),
]

const tabValue = ref<'login' | 'register'>('login')
const isNavigating = ref(false)

const textureMaskParams = reactive({
  size: '666px 666px',
  x: 0,
  y: 0,
})

const textureStyle = computed(() => ({
  filter: isDark.value ? 'invert(0.18)' : 'invert(0.9)',
  maskImage: `radial-gradient(circle 200px at ${textureMaskParams.x}px ${textureMaskParams.y}px, #f0f 0%, transparent 100%)`,
  WebkitMaskImage: `radial-gradient(circle 200px at ${textureMaskParams.x}px ${textureMaskParams.y}px, #f0f 0%, transparent 100%)`,
}))

const loginFormRef = useTemplateRef<InstanceType<typeof NForm>>('loginFormRef')
const registerFormRef = useTemplateRef<InstanceType<typeof NForm>>('registerFormRef')

const loginForm = reactive({ username: '', password: '' })
const registerForm = reactive({ username: '', password: '', confirmPassword: '', name: '', phone: '' })

const loginRules: Record<string, FormItemRule[]> = {
  username: [{ required: true, message: '请输入用户名', trigger: ['input'] }],
  password: [{ required: true, message: '请输入密码', trigger: ['input'] }],
}
const registerRules: Record<string, FormItemRule[]> = {
  username: [{ required: true, message: '请输入用户名', trigger: ['input'] }],
  password: [{ required: true, message: '请输入密码', trigger: ['input'] }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: ['input'] },
    {
      validator: (_rule: FormItemRule, value: string) => value === registerForm.password,
      message: '两次密码不一致',
      trigger: ['input', 'blur'],
    },
  ],
  name: [{ required: true, message: '请输入姓名', trigger: ['input'] }],
  phone: [{ required: true, message: '请输入联系方式', trigger: ['input'] }],
}

const loading = computed(() => isNavigating.value)

function navigateByRole(role: string) {
  isNavigating.value = true
  const path = role === 'admin' ? '/admin/jobs' : '/student/jobs'
  router.replace({ path }).finally(() => { isNavigating.value = false })
}

async function handleLogin() {
  loginFormRef.value?.validate(async (errors) => {
    if (errors) return
    try {
      const res = await userStore.userLogin(loginForm)
      if (res.code === 200 && res.data) {
        message.success('登录成功')
        navigateByRole(res.data.role)
      } else {
        message.error('用户名或密码错误')
      }
    } catch {
      message.error('登录失败，请检查网络')
    }
  })
}

async function handleRegister() {
  registerFormRef.value?.validate(async (errors) => {
    if (errors) return
    try {
      const res = await userStore.userRegister({
        username: registerForm.username,
        password: registerForm.password,
        name: registerForm.name,
        phone: registerForm.phone,
      })
      if (res.code === 200 && res.data) {
        message.success('注册成功')
        navigateByRole(res.data.role)
      }
    } catch {
      message.error('注册失败')
    }
  })
}

function updateTexturePosition(x: number, y: number) {
  textureMaskParams.x = x
  textureMaskParams.y = y
}
function onMouseMove(e: MouseEvent) { updateTexturePosition(e.clientX, e.clientY) }
function onTouchMove(e: TouchEvent) { if (e.touches[0]) updateTexturePosition(e.touches[0].clientX, e.touches[0].clientY) }

onMounted(() => {
  window.addEventListener('mousemove', onMouseMove)
  window.addEventListener('touchmove', onTouchMove)
})
onUnmounted(() => {
  window.removeEventListener('mousemove', onMouseMove)
  window.removeEventListener('touchmove', onTouchMove)
})
</script>
<template>
  <div class="relative flex h-svh items-center justify-center overflow-hidden bg-neutral-25 p-6 transition-[background-color] dark:bg-neutral-900">
    <div
      class="absolute top-0 left-0 size-full bg-neutral-200/45 transition-[background-color] dark:bg-neutral-800/50"
      :style="{ 'mask-image': `url(${topographySvg})`, '-webkit-mask-image': `url(${topographySvg})`, 'mask-repeat': 'repeat', 'mask-size': textureMaskParams.size, '-webkit-mask-repeat': 'repeat', '-webkit-mask-size': textureMaskParams.size }"
    />
    <div
      class="pointer-events-none absolute inset-0 z-10 transition-[filter]"
      :style="{ 'background-image': `url(${topographySvg})`, 'background-size': textureMaskParams.size, '-webkit-mask-repeat': 'no-repeat', 'mask-repeat': 'no-repeat', ...textureStyle }"
    />
    <div class="relative z-50 flex h-[560px] w-[800px] justify-center rounded shadow-lg">
      <div v-if="!isMaxSm" class="flex-1 bg-neutral-35 py-6 pl-6 text-primary transition-[background-color] dark:bg-neutral-825">
        <NCarousel draggable :show-dots="false" :show-arrow="false" loop autoplay>
          <div v-for="(illustration, index) in illustrations" :key="index" class="flex h-full items-center p-5">
            <component :is="illustration" />
          </div>
        </NCarousel>
      </div>
      <div class="relative flex w-full flex-col bg-white px-10 py-8 transition-[background-color] sm:w-[360px] dark:bg-neutral-800">
        <div class="absolute top-0 left-0 z-50 flex w-full items-center justify-end gap-x-4 p-4">
          <ThemeColorPopover />
          <ThemeModePopover />
        </div>
        <div class="flex-1 flex flex-col justify-center">
          <h2 class="text-2xl font-bold text-center mb-1">大学生兼职平台</h2>
          <p class="text-neutral-400 text-center text-sm mb-6 transition-[color] dark:text-neutral-500">Part-time Job Platform</p>

          <NTabs v-model:value="tabValue" type="line" animated justify-content="center" class="sign-in-tabs">
            <NTab name="login" tab="登录" />
            <NTab name="register" tab="注册" />
          </NTabs>

          <div v-show="tabValue === 'login'" class="mt-6">
            <NForm ref="loginFormRef" :model="loginForm" :show-require-mark="false" :rules="loginRules">
              <NFormItem path="username" label="用户名">
                <NInput v-model:value="loginForm.username" placeholder="请输入用户名" clearable
                  :theme-overrides="isDark ? { color: twColor('neutral', 750), border: `1px solid ${twColor('neutral', 700)}` } : undefined">
                  <template #prefix><span class="mr-1.5 iconify size-5.5 ph--user-circle" /></template>
                </NInput>
              </NFormItem>
              <NFormItem path="password" label="密码">
                <NInput v-model:value="loginForm.password" placeholder="请输入密码" type="password" clearable
                  :theme-overrides="isDark ? { color: twColor('neutral', 750), border: `1px solid ${twColor('neutral', 700)}` } : undefined">
                  <template #prefix><span class="mr-1.5 iconify size-5.5 ph--lock-key" /></template>
                </NInput>
              </NFormItem>
              <div class="mt-6">
                <NButton type="primary" :loading="loading" :disabled="loading" attr-type="button" block size="medium" @click="handleLogin">
                  登&nbsp;录
                </NButton>
              </div>
            </NForm>
          </div>

          <div v-show="tabValue === 'register'" class="mt-6" style="overflow-y: auto; max-height: 300px; padding-right: 4px;">
            <NForm ref="registerFormRef" :model="registerForm" :show-require-mark="false" :rules="registerRules">
              <NFormItem path="username" label="用户名">
                <NInput v-model:value="registerForm.username" placeholder="请输入用户名" clearable />
              </NFormItem>
              <NFormItem path="name" label="姓名">
                <NInput v-model:value="registerForm.name" placeholder="请输入姓名" clearable />
              </NFormItem>
              <NFormItem path="phone" label="联系方式">
                <NInput v-model:value="registerForm.phone" placeholder="请输入手机号" clearable />
              </NFormItem>
              <NFormItem path="password" label="密码">
                <NInput v-model:value="registerForm.password" placeholder="请输入密码" type="password" clearable />
              </NFormItem>
              <NFormItem path="confirmPassword" label="确认密码">
                <NInput v-model:value="registerForm.confirmPassword" placeholder="请确认密码" type="password" clearable />
              </NFormItem>
              <div class="mt-4">
                <NButton type="primary" :loading="loading" :disabled="loading" attr-type="button" block size="medium" @click="handleRegister">
                  注&nbsp;册
                </NButton>
              </div>
            </NForm>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.sign-in-tabs :deep(.n-tabs-tab) {
  min-width: 100px;
  justify-content: center;
}
</style>
