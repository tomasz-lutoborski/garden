<script setup lang="ts">
import { ref } from 'vue'

const emit = defineEmits<{
  (e: 'submit', title: string): void
}>()

const value = ref('')

function submit() {
  const title = value.value.trim()
  if (!title) return
  emit('submit', title)
  value.value = ''
}
</script>

<template>
  <form @submit.prevent="submit" class="flex flex-row justify-between">
    <input v-model="value" type="text" placeholder="New task title" class="border rounded p-2" />
    <button type="submit" class="px-4 py-2 border rounded" :disabled="!value.trim()">
      Add Task
    </button>
  </form>
</template>
