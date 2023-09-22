export type ImagesLoaded = {
  [key: string]: HTMLImageElement;
};

/**
 * Load an image asynchronously
 */
export function loadImage(src: string): Promise<HTMLImageElement> {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.src = src;
    img.addEventListener(
      "load",
      () => {
        resolve(img);
      },
      false
    );
    img.addEventListener(
      "error",
      (err) => {
        reject(err);
      },
      false
    );
  });
}

/**
 * Preload a bunch of images with names
 */
export async function loadImages(
  images: { name: string; src: string }[]
): Promise<ImagesLoaded> {
  const htmlImages = await Promise.all(
    images.map((image) => loadImage(image.src))
  );
  const imagesLoaded: ImagesLoaded = {};
  images.forEach((image, index) => {
    imagesLoaded[image.name] = htmlImages[index];
  });
  return imagesLoaded;
}
