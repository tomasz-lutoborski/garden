<script setup lang="ts">
import Tasks from './features/tasks/Tasks.vue'
</script>

<template>
  <Tasks />
</template>

<style>
#app {
  inline-size: 100%; /* avoid 100vw; it includes scrollbar width */
  min-block-size: 100svh; /* small-viewport height: stable against URL bar show/hide */
  block-size: 100dvh; /* dynamic vh as the primary fill */
  padding: env(safe-area-inset-top, 0px) env(safe-area-inset-right, 0px)
    env(safe-area-inset-bottom, 0px) env(safe-area-inset-left, 0px);
}
</style>
