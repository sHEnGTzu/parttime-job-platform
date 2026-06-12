import { routerEventBus } from '@/event-bus'
import { useUserStore } from '@/stores'

import type { Router } from 'vue-router'

export function setupRouterGuard(router: Router) {
  const userStore = useUserStore()

  router.beforeEach((to) => {
    routerEventBus.emit({ type: 'beforeEach' })

    userStore.loadFromStorage()

    if (to.name === 'signIn') {
      return
    }

    if (!userStore.token) {
      return { name: 'signIn' }
    }
  })

  router.beforeResolve(() => {})

  router.afterEach(() => {
    routerEventBus.emit({ type: 'afterEach' })
  })
}
