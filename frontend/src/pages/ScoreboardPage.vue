<script lang="ts" setup>
import { useRoute, useRouter } from "vue-router";
import { gameState } from "../hooks/websocket";
import { computed } from "vue";
import { formatSurvivalTime, sortPlayers } from "../utils/score";

const router = useRouter();
const route = useRoute();

const players = computed(() => sortPlayers(gameState.players));

const goBackToWaitingRoom = () => {
  router.push(`/waiting?name=${route.query.name}`);
};
</script>

<template>
  <div class="layout">
    <div class="winner">
      <h2>
        Congrats <strong>{{ players[0].name }}</strong
        >, <br />
        you won!
      </h2>
      <iframe
        title="winner"
        src="https://giphy.com/embed/2gtoSIzdrSMFO"
        width="120"
        height="90"
        frameBorder="0"
      ></iframe>
    </div>
    <table class="scoreboard">
      <thead>
        <tr class="row header-row">
          <td>
            <strong>Name</strong>
          </td>
          <td>
            <strong># Players Infected</strong>
          </td>
          <td>
            <strong>Survival Time</strong>
          </td>
          <td>
            <strong>Score</strong>
          </td>
        </tr>
      </thead>

      <tbody>
        <tr class="row" v-for="player in players">
          <td>
            <span v-if="player.id === gameState.playerId">⭐</span>
            {{ player.name }}
          </td>
          <td>
            {{ player.playerStats.numberOfInfectedPeople }}
          </td>
          <td class="time-column">
            {{ formatSurvivalTime(player.playerStats.survivalTime) }}
          </td>
          <td>
            {{ player.playerStats.score }}
          </td>
        </tr>
      </tbody>
    </table>

    <button class="wait-btn w-100" @click="goBackToWaitingRoom">
      Go to waiting room
    </button>
  </div>
</template>

<style scoped>
.winner {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-bottom: 20px;
}
.scoreboard {
  border-collapse: collapse;
  overflow: auto;
  border: 1px solid pink;
  border-radius: 4px;
  width: 100%;
}

.row {
  height: 50px;
  border-bottom: 1px solid var(--primary-color);
}

.header-row {
  background-image: linear-gradient(135deg, #81ffef 10%, #f067b4 100%);
  color: rgba(10, 10, 10, 0.8);
  position: sticky;
  top: 0;
  border-bottom-width: 2px;
}

.wait-btn {
  margin-top: 10px;
}

.time-column {
  text-align: right;
}
</style>
