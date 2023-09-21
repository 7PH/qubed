<script setup lang="ts">
import { PlayerStatus } from "../types";
import { useWebSocket } from "../hooks/websocket";

const { playerId, playerList, setReady } = useWebSocket();

function handleReadyClick() {
  setReady();
}
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
          <span
            :style="{ fontWeight: player.id === playerId ? 600 : 'normal' }"
            >{{ player.name }}</span
          >
        </td>
        <td class="status">
          <span
            class="badge"
            v-if="
              player.id !== playerId || player.status === PlayerStatus.Ready
            "
            >{{
              player.status === PlayerStatus.Ready ? "✅ Ready" : "⏳ Wait up"
            }}</span
          >

          <button
            v-if="
              player.id === playerId && !(player.status === PlayerStatus.Ready)
            "
            @click="handleReadyClick"
          >
            Ready?
          </button>
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
