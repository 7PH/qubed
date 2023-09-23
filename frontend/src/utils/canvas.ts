/**
 * Resize canvas to fit its container
 */
export function resizeCanvas(
  canvas: HTMLCanvasElement,
  container: HTMLDivElement
) {
  canvas.width = container.offsetWidth;
  // Canvas is always a square
  canvas.height = canvas.width;
}
