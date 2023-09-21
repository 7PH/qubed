import { onMounted, onUnmounted, ref } from "vue";

/**
 * Mapping between key codes and key names
 */
const KEY_MAP: Record<string, string> = {
  ArrowLeft: "left",
  ArrowUp: "up",
  ArrowRight: "right",
  ArrowDown: "down",
};

export type ArrowKeyChange = { [key: string]: boolean };

export type CallbackType = (args: ArrowKeyChange) => unknown;

export const useArrowKeys = (callback: CallbackType) => {
  const keys = ref<Record<string, boolean>>({});

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
    callback({
      [key]: pressed,
    });
  }
};
