<script setup lang="ts">
import { computed, onMounted, onUnmounted } from "vue";
import { useWebSocket } from "../hooks/websocket";
import { PlayerStatus } from "../types";

const MIN_PLAYERS_NUMBER = 3;

const { playerId, playerList, connect, disconnect, setReady, startGame } =
  useWebSocket();

onMounted(() => {
  disconnect(); // disconnect if possible
  connect();
});

function handleReadyClick() {
  setReady();
}

function handleStart() {
  startGame();
}

function handleEnter(event: KeyboardEvent) {
  if (event.key === "Enter") {
    handleReadyClick();
  }
}

onMounted(() => {
  document.addEventListener("keypress", handleEnter);
});

onUnmounted(() => {
  document.removeEventListener("keypress", handleEnter);
});

const canStart = computed(
  () =>
    playerList.value.filter((p) => p.status === PlayerStatus.Ready).length >=
    MIN_PLAYERS_NUMBER
);
</script>

<template>
  <div class="layout">
    <h3>Room:</h3>

    <table class="playerlist" col>
      <thead>
        <tr>
          <th>Players ({{ playerList.length }})</th>
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

    <div style="margin-top: 2rem">
      <button class="w-100" :disabled="!canStart" @click="handleStart">
        Start the game!
      </button>
    </div>
  </div>
</template>

<style scoped>
.playerlist {
  width: 100%;
  border-radius: var(--radius);
  border: var(--border);
  background: var(--background-secondary);
  padding: 1rem;
  text-align: left;

  tr {
    padding: 8px 0;
  }
}

td {
  width: 50%;
  height: 45px;
}
</style>
