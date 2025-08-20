import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { uid } from '@/lib/uid'

export type Task = { id: string; title: string }

export const useWorkflow = defineStore('workflow', () => {
  const tasks = ref<Record<string, Task>>({})
  const bankIds = ref<string[]>([])
  const daily = ref<(string | null)[]>([null, null, null])
  const activeTaskId = ref<string | null>(null)

  const session = ref({
    status: 'idle' as 'idle' | 'focusing' | 'breaking',
    startedAt: null as number | null,
    focusTotalMs: 0,
    breakTotalMs: 0,
  })

  const bank = computed(() => bankIds.value.map((id) => tasks.value[id]))
  const dailySlots = computed(() => daily.value.map((id) => (id ? tasks.value[id] : null)))
  const activeTask = computed(() => (activeTaskId.value ? tasks.value[activeTaskId.value] : null))

  function seed() {
    if (bank.value.length) return
    ;['read a book', 'finish project design', 'play a piano'].forEach((title) => {
      const id = uid()
      tasks.value[id] = { id, title }
      bankIds.value.push(id)
    })
  }

  function addTask(title: string) {
    const id = uid()
    tasks.value[id] = { id, title }
    bankIds.value.push(id)
  }

  function moveToDaily(taskId: string, slotIndex: 0 | 1 | 2) {
    daily.value[slotIndex] = taskId
    bankIds.value = bankIds.value.filter((id) => id !== taskId)
  }

  function clearDailySlot(slotIndex: 0 | 1 | 2) {
    const taskId = daily.value[slotIndex]
    if (taskId) {
      daily.value[slotIndex] = null
      bankIds.value.push(taskId)
    }
  }

  function activateFromDaily(slotIndex: 0 | 1 | 2) {
    const taskId = daily.value[slotIndex]
    if (taskId) {
      activeTaskId.value = taskId
      daily.value[slotIndex] = null
    }
  }

  function resetDay() {
    daily.value.forEach((id) => {
      if (id) bankIds.value.push(id)
    })
    daily.value = [null, null, null]
    activeTaskId.value = null
    session.value = {
      status: 'idle',
      startedAt: null,
      focusTotalMs: 0,
      breakTotalMs: 0,
    }
  }

  return {
    // state
    tasks,
    bankIds,
    daily,
    activeTaskId,
    session,
    // computed
    bank,
    dailySlots,
    activeTask,
    // actions
    seed,
    addTask,
    moveToDaily,
    clearDailySlot,
    activateFromDaily,
    resetDay,
  }
})
