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
    <div class="scoreboard">
      <div class="row header-row">
        <div class="column">
          <strong>Name</strong>
        </div>
        <div class="column">
          <strong># Players Infected</strong>
        </div>
        <div class="column">
          <strong>Survival Time</strong>
        </div>
      </div>

      <div class="row" v-for="player in players">
        <div class="column">{{ player.name }}</div>
        <div class="column">
          {{ player.playerStats.numberOfInfectedPeople }}
        </div>
        <div class="column">
          {{ formatSurvivalTime(player.playerStats.survivalTime) }}
        </div>
      </div>
    </div>

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
  overflow: auto;
  border: 1px solid pink;
  border-radius: 4px;
  height: 70vb;
}

.row {
  display: flex;
  padding: 10px;
  border-bottom: 1px solid var(--primary-color);
}

.header-row {
  position: sticky;
  background-color: var(--background-secondary);
  top: 0;
}

.column {
  flex-grow: 1;
}

.wait-btn {
  margin-top: 10px;
}
</style>
