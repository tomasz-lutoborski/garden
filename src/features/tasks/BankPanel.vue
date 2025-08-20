<script setup lang="ts">
import { useWorkflow } from './store'
import Add from './Add.vue'
import Bank from './Bank.vue'

const wf = useWorkflow()

function onAdd(title: string) {
  const exists = wf.bank.some((task) => task.title === title)
  if (!exists) wf.addTask(title)
}

function onMoveToSlot(payload: { taskId: string; slot: 0 | 1 | 2 }) {
  if (!wf.daily[payload.slot]) wf.moveToDaily(payload.taskId, payload.slot)
}
</script>
<template>
  <div class="flex flex-col gap-12">
    <Bank @move-to-slot="onMoveToSlot" :tasks="wf.bank" />
    <Add @submit="onAdd" />
  </div>
</template>
