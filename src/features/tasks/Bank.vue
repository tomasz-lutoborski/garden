<script setup lang="ts">
import type { Task } from '@/features/tasks/store'

type Slot = 0 | 1 | 2

const props = defineProps<{
  tasks: readonly Task[]
}>()

const emit = defineEmits<{
  (e: 'move-to-slot', payload: { taskId: string; slot: Slot }): void
}>()
</script>
<template>
  <div class="mt-10">
    <h3 class="text-lg font-semibold">Task bank</h3>
    <div v-if="props.tasks.length" class="mt-3 space-y-3">
      <div
        v-for="t in props.tasks"
        :key="t.id"
        class="border rounded-lg p-3 flex items-center justify-between gap-4"
      >
        <span>{{ t.title }}</span>
        <div class="flex gap-4">
          <button
            class="px-2 py-1 border rounded"
            @click="emit('move-to-slot', { taskId: t.id, slot: 0 })"
          >
            to 1
          </button>
          <button
            class="px-2 py-1 border rounded"
            @click="emit('move-to-slot', { taskId: t.id, slot: 1 })"
          >
            to 2
          </button>
          <button
            class="px-2 py-1 border rounded"
            @click="emit('move-to-slot', { taskId: t.id, slot: 2 })"
          >
            to 3
          </button>
        </div>
      </div>
    </div>
    <div v-else class="text-gray-500 mt-3">Bank empty.</div>
  </div>
</template>
