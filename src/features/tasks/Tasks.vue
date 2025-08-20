<script setup lang="ts">
import { useWorkflow } from './store'
import TaskBankPanel from './BankPanel.vue'
import ActiveTask from './Active.vue'
import DailyTasks from './Daily.vue'

const wf = useWorkflow()
wf.seed()

type Slot = 0 | 1 | 2

function clearSlot(slot: Slot) {
  wf.clearDailySlot(slot)
}

function activate(slot: Slot) {
  wf.activateFromDaily(slot)
}
</script>

<template>
  <header class="bg-white shadow app-header">
    <h1 class="text-2xl font-bold p-4">Task Management</h1>
  </header>

  <section class="app-left p-4 pane">
    <DailyTasks @activate="activate" @clear="clearSlot" :daily-slots="wf.dailySlots" />
  </section>

  <section class="app-main p-4 pane">
    <ActiveTask :active-task="wf.activeTask" />
  </section>

  <section class="app-right p-4 pane">
    <TaskBankPanel />
  </section>
</template>
