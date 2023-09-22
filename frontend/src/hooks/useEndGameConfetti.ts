import JSConfetti from "js-confetti";
import { onMounted, onUnmounted, ref } from "vue";
import { gameState } from "./websocket";

export function useEndGameConfetti() {
  const mounted = ref(false);

  onMounted(() => {
    mounted.value = true;
    requestAnimationFrame(tick);
  });

  onUnmounted(() => {
    mounted.value = false;
  });

  function showConfetti() {
    setTimeout(() => {
      new JSConfetti().addConfetti();
    }, 500);
  }

  function tick() {
    if (! mounted.value) {
      return;
    }
    if (gameState.gameFinished) {
      showConfetti();
      return;
    }
    requestAnimationFrame(tick);
  }
}
