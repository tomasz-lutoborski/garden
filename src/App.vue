<script setup lang="ts">
import { useWorkflow } from './stores/workflow'

const wf = useWorkflow()
wf.seed()

const slotIdx = [0, 1, 2] as const
type Slot = (typeof slotIdx)[number]

function toSlot(taskId: string, slot: Slot) {
  wf.moveToDaily(taskId, slot)
}

function clearSlot(slot: Slot) {
  wf.clearDailySlot(slot)
}

function activate(slot: Slot) {
  wf.activateFromDaily(slot)
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
      <!-- Left: Daily slots -->
      <section>
        <h2 class="text-2xl font-semibold py-4">Daily slots</h2>
        <div class="mt-4 space-y-6 flex flex-col gap-4">
          <div
            v-for="i in slotIdx"
            :key="i"
            class="border rounded-xl p-4 min-h-[88px] flex items-center justify-between gap-4"
          >
            <div class="flex-1">
              <div v-if="wf.dailySlots[i]" class="text-lg font-medium">
                {{ wf.dailySlots[i]?.title }}
              </div>
              <div v-else class="text-gray-500">Empty</div>
            </div>
            <div class="shrink-0 flex gap-2">
              <button
                class="px-3 py-1 border rounded"
                @click="activate(i)"
                :disabled="!wf.dailySlots[i]"
              >
                Activate
              </button>
              <button
                class="px-3 py-1 border rounded"
                @click="clearSlot(i)"
                :disabled="wf.dailySlots[i] === null"
              >
                Clear
              </button>
            </div>
          </div>
        </div>
      </section>

      <!-- Right: Active slot + Task bank -->
      <section class="flex flex-col gap-4">
        <h2 class="text-2xl font-semibold">Active slot</h2>
        <div class="mt-4 border rounded-xl p-6 min-h-[120px] flex items-center justify-center">
          <div v-if="wf.activeTask" class="text-xl font-semibold text-center">
            {{ wf.activeTask.title }}
          </div>
          <div v-else class="text-gray-500">No active task</div>
        </div>

      </section>
    </div>
  </div>
</template>
