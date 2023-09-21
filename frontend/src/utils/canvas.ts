export function resizeCanvas(
  canvas: HTMLCanvasElement,
  container: HTMLDivElement
) {
  canvas.width = container.offsetWidth;
  canvas.height = canvas.width;
}
