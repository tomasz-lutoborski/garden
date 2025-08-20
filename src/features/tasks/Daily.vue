<script setup lang="ts">
import type { useWorkflow } from './store'

const slotIdx = [0, 1, 2] as const
type Slot = (typeof slotIdx)[number]

const props = defineProps<{
  dailySlots: ReturnType<typeof useWorkflow>['dailySlots']
}>()

const emit = defineEmits<{
  (e: 'activate', slot: Slot): void
  (e: 'clear', slot: Slot): void
}>()
</script>
<template>
  <h2 class="text-2xl font-semibold py-4">Daily slots</h2>
  <div class="mt-4 space-y-6 flex flex-col gap-4">
    <div
      v-for="i in slotIdx"
      :key="i"
      class="border rounded-xl p-4 min-h-[88px] flex items-center justify-between gap-4"
    >
      <div class="flex-1">
        <div v-if="props.dailySlots[i]" class="text-lg font-medium">
          {{ props.dailySlots[i]?.title }}
        </div>
        <div v-else class="text-gray-500">Empty</div>
      </div>
      <div class="shrink-0 flex gap-2">
        <button class="px-3 py-1 border rounded" @click="emit('activate', i)">Activate</button>
        <button
          class="px-3 py-1 border rounded"
          @click="emit('clear', i)"
          :disabled="props.dailySlots[i] === null"
        >
          Clear
        </button>
      </div>
    </div>
  </div>
</template>
