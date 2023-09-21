<script setup lang="ts">
import { useGameRenderer } from '../hooks/useGameRenderer';

const { container } = useGameRenderer();
</script>

<template>
  <div class="game-container">
    <div ref="container"></div>
  </div>
</template>

<style scoped>
.game-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
