import { acceptHMRUpdate, defineStore } from 'pinia'
import { computed, ref } from 'vue'
import type { MenuOption } from 'naive-ui'
import { h } from 'vue'

import { login, register, getUserById } from '@/api/parttime'
import router from '@/router'
import { useTabsStore } from './tabs'

import type { UserInfo } from '@/api/parttime'

function renderIcon(icon: string) {
  return () => h('span', { class: `iconify ${icon} size-5` })
}

export const useUserStore = defineStore('userStore', () => {
  const user = ref<UserInfo | null>(null)
  const token = ref<string | null>(null)

  function setUser(userInfo: UserInfo) {
    user.value = userInfo
    token.value = 'logged'
    localStorage.setItem('user', JSON.stringify(userInfo))
    localStorage.setItem('token', 'logged')
  }

  function loadFromStorage() {
    const saved = localStorage.getItem('user')
    const savedToken = localStorage.getItem('token')
    if (saved && savedToken) {
      user.value = JSON.parse(saved)
      token.value = savedToken
    }
  }

  async function userLogin(data: { username: string; password: string }) {
    const res = await login(data)
    if (res.code === 200 && res.data) {
      setUser(res.data)
    }
    return res
  }

  async function userRegister(data: { username: string; password: string; name: string; phone: string }) {
    const res = await register(data)
    if (res.code === 200 && res.data) {
      setUser(res.data)
    }
    return res
  }

  async function refreshUser() {
    if (user.value?.id) {
      const res = await getUserById(user.value.id)
      if (res.code === 200 && res.data) {
        user.value = res.data
        localStorage.setItem('user', JSON.stringify(res.data))
      }
    }
  }

  function cleanup(redirectPath?: string) {
    const tabsStore = useTabsStore()
    tabsStore.clearTabs()
    router.replace({
      name: 'signIn',
      ...(redirectPath ? { query: { r: redirectPath } } : {}),
    })
    token.value = null
    user.value = null
    localStorage.removeItem('user')
    localStorage.removeItem('token')
  }

  const userMenu = computed<MenuOption[]>(() => {
    if (!user.value) return []
    const role = user.value.role
    const common: MenuOption[] = [
      {
        label: '数据概览',
        key: 'dashboard',
        icon: renderIcon('ph--chart-bar'),
      },
    ]
    if (role === 'admin') {
      return [
        ...common,
        {
          label: '兼职管理',
          key: 'adminJobs',
          icon: renderIcon('ph--briefcase'),
        },
        {
          label: '申请管理',
          key: 'adminApplies',
          icon: renderIcon('ph--clipboard-text'),
        },
      ]
    }
    return [
      ...common,
      {
        label: '兼职信息',
        key: 'studentJobs',
        icon: renderIcon('ph--briefcase'),
      },
      {
        label: '我的申请',
        key: 'studentApplies',
        icon: renderIcon('ph--file-text'),
      },
    ]
  })

  return {
    user,
    token,
    userMenu,
    userLogin,
    userRegister,
    refreshUser,
    loadFromStorage,
    cleanup,
    setUser,
  }
})

export function toRefsUserStore() {
  const store = useUserStore()
  return {
    user: store.user,
    token: store.token,
  }
}

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}
