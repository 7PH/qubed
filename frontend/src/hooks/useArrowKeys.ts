import { onMounted, onUnmounted, ref } from "vue";

/**
 * Mapping between key names as they come from the event handler and key names as the backend knows them.
 */
const KEY_MAP: Record<string, string> = {
  ArrowLeft: "left",
  ArrowUp: "up",
  ArrowRight: "right",
  ArrowDown: "down",
  a: "left",
  w: "up",
  d: "right",
  s: "down",
};

export type CallbackType = (args: { [key: string]: boolean }) => unknown;

export const useArrowKeys = (callback: CallbackType) => {
  const keys = ref<{ [key: string]: boolean }>({});

  onMounted(() => {
    window.addEventListener("keydown", handle);
    window.addEventListener("keyup", handle);
  });

  onUnmounted(() => {
    window.removeEventListener("keydown", handle);
    window.removeEventListener("keyup", handle);
  });

  function handle(event: KeyboardEvent) {
    if (!(event.key in KEY_MAP)) {
      return;
    }
    if (event.repeat) {
      return;
    }
    const key = KEY_MAP[event.key];
    const pressed = event.type === "keydown";
    keys.value[key] = pressed;
    callback(keys.value);
  }
};
