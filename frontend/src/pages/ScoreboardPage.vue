<script lang="ts" setup>
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { gameState, useWebSocket } from '../hooks/websocket';

const router = useRouter()
const route = useRoute()
const { disconnect } = useWebSocket();

const mockedUsers = Array.from({ length: 20}).map((_, index) => ({
  name: `Player${index +1}`,
  place: index + 1
}))
const winner = gameState.players.find((player) => !player.infected)?.name || '';

const goBackToWaitingRoom = () => {
  router.push(`/waiting?name=${route.query.name}`)
}

onMounted(disconnect);

</script>

<template>
  <div class="layout">
    <div class="winner">
      <h2>Congrats {{winner.toUpperCase()}}, <br/> you won!</h2>
      <iframe title="winner" src="https://giphy.com/embed/2gtoSIzdrSMFO" width="120" height="90" frameBorder="0" ></iframe>
    </div>
    <div class="scoreboard">
        <div class="row header-row">
          <div class="column">
            <strong>Name</strong>
          </div>
          <div class="column">
            <strong>Place</strong>
          </div>
        </div>

        <div class="row" v-for="(user) in mockedUsers">
          <div class="column">{{user.name}}</div>
          <div class="column">{{user.place}}</div>
        </div>
    </div>

    <button class="wait-btn w-100" @click="goBackToWaitingRoom">Go to waiting room</button>
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
  /* background-color: var(--background-secondary); */
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