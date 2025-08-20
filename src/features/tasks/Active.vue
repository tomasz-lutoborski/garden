<script setup lang="ts">
import type { useWorkflow } from './store'

const props = defineProps<{
  activeTask: ReturnType<typeof useWorkflow>['activeTask']
}>()
</script>
<template>
  <h2 class="text-2xl font-semibold">Active slot</h2>
  <div class="mt-4 border rounded-xl p-6 min-h-[120px] flex items-center justify-center">
    <div v-if="props.activeTask" class="text-xl font-semibold text-center">
      {{ props.activeTask.title }}
    </div>
    <div v-else class="text-gray-500">No active task</div>
  </div>
</template>
