import { computed, onMounted, onUnmounted, ref } from "vue";
import { resizeCanvas } from "../utils/canvas";
import { drawGame } from "../utils/draw";
import { useArrowKeys } from "./useArrowKeys";
import { gameState, sendCommands } from "./websocket";

export const useGameRenderer = () => {
  // Ticking logic
  const ticking = ref(false);

  // Canvas ref
  const containerRef = ref<HTMLDivElement | null>(null);
  const canvasRef = ref<HTMLCanvasElement | null>(null);
  const contextRef = computed(() => canvasRef.value?.getContext("2d"));

  // Keyboard state
  useArrowKeys(sendCommands);

  // Use ResizeObserver to detect when container size changes
  const observer = new ResizeObserver(() => {
    if (!canvasRef.value || !containerRef.value) {
      return;
    }
    resizeCanvas(canvasRef.value, containerRef.value);
  });

  // Create canvas when component mounts
  onMounted(() => {
    if (!containerRef.value) {
      return;
    }
    ticking.value = true;
    canvasRef.value = document.createElement("canvas");
    containerRef.value.appendChild(canvasRef.value);
    resizeCanvas(canvasRef.value, containerRef.value);
    observer.observe(containerRef.value);
    requestAnimationFrame(tick);
  });

  // Cleanup
  onUnmounted(() => {
    ticking.value = false;
    if (!containerRef.value || !canvasRef.value) {
      return;
    }
    containerRef.value.removeChild(canvasRef.value);
    observer.unobserve(containerRef.value);
    canvasRef.value = null;
  });

  // Tick function
  function tick() {
    if (!ticking.value || !canvasRef.value || !contextRef.value) {
      return;
    }

    contextRef.value.reset();
    drawGame(canvasRef.value, contextRef.value, gameState);

    ticking.value && requestAnimationFrame(tick);
  }

  return { container: containerRef };
};
