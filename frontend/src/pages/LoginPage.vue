<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { useRouter } from 'vue-router';
import { useWebSocket } from '../hooks/websocket';

const router = useRouter()
const { disconnect } = useWebSocket()
const username = ref("");


function handleEnter() {
 router.push(`/waiting?name=${username.value}`) 
}

onMounted(() => {
  disconnect()
})
</script>

<template>
  <div class="layout">
    <iframe
      src="https://giphy.com/embed/qHIj9ACRey9yBtgwD9"
      title="bug"
      width="250"
      height="250"
      frameBorder="0"
    ></iframe>
    <h1>Welcome to 'Qubed'</h1>

    <hr style="border-color: pink" />

    <h3>Rules:</h3>

    <span
      >This game is simple! <br style="margin-top: 8px" />
      When it starts, one "Qubed" will be randomly chosen. His goal is to make
      all other guys "Qubed" too, while other players need to run away. The game
      ends, when only one "non-qubed" player is left, who is the winner.</span
    >
    <hr style="border-color: pink" />

    <input
      style="margin-top: 50px"
      type="text"
      id="username"
      placeholder="Enter a username and press enter"
      v-model="username"
      @keyup.enter="handleEnter"
    />
  </div>
</template>
