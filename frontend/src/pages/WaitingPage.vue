<script setup lang="ts">
import { useWebSocket } from "../hooks/websocket";

const { playerList } = useWebSocket();
</script>

<template>
  <div class="layout">
    <table class="playerlist">
      <thead>
        <tr>
          <th>Player</th>
          <th>State</th>
        </tr>
      </thead>
      <tr v-for="player in playerList">
        <td>
          {{ player.name }}
        </td>
        <td class="status">
          <span class="badge">{{
            player.ready ? "✅ Ready" : "⏳ Wait up"
          }}</span>
        </td>
      </tr>
    </table>
  </div>
</template>

<style scoped>
.playerlist {
  width: 100%;
  border-radius: 10px;
  background-image: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
  padding: 1rem;
  text-align: left;

  tr {
    padding: 8px 0;
  }
}
</style>
