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
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.game-container > div {
  max-height: 1200px;
  aspect-ratio: 1 / 1;
  flex-grow: 1;
  overflow: hidden;
}
</style>
