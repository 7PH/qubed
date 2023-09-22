import { Ref, onMounted, onUnmounted, ref } from "vue";
import { ImagesLoaded, loadImages } from "../utils/image";

export function useGameInitialize() {
  const initialized = ref(false);
  const images: Ref<ImagesLoaded> = ref({});

  onMounted(async () => {
    images.value = await loadImages([
      {
        name: "bush",
        src: "bush.png",
      },
      {
        name: "bush2",
        src: "bush2.png",
      },
      {
        name: "cloud",
        src: "cloud.png",
      },
    ]);
    initialized.value = true;
  });

  onUnmounted(() => {
    initialized.value = false;
  });

  return { initialized, images };
}
