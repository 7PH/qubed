import { computed, onMounted, onUnmounted, ref } from "vue";
import { Player } from "../types";
import { resizeCanvas } from "../utils/canvas";
import { drawGame } from "../utils/draw";
import { useArrowKeys } from "./useArrowKeys";
import { sendCommands } from "./websocket";

const DUMMY_PLAYERS: Player[] = [
  {
    id: 123,
    name: "Player 1",
    position: [0.5, 0.5],
    infected: false,
    ready: true,
  },
  {
    id: 123,
    name: "Player 2",
    position: [0.67, 0.55],
    infected: true,
    ready: true,
  },
];

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
    drawGame(canvasRef.value, contextRef.value, DUMMY_PLAYERS);

    ticking.value && requestAnimationFrame(tick);
  }

  return { container: containerRef };
};
