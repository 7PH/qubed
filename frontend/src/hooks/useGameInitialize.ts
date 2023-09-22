import { computed, ref } from "vue";

export const useGameInitialize = () => {
  const imagesLoaded = ref<{ [key: string]: boolean }>({});

  const bushImg = new Image();
  bushImg.src = "public/bush.png";

  const images = [bushImg];

  images.forEach((img) => {
    img.addEventListener(
      "load",
      () => {
        imagesLoaded.value[img.src] = true;
      },
      false
    );
  });

  const initialized = computed(() =>
    Object.values(imagesLoaded.value).every((val) => val)
  );

  return { initialized, images };
};
