import { Player } from "../types";

const FULL_CIRCLE = 2 * Math.PI;

const PLAYER_USERNAME_FONT_SIZE = 0.8 / 100; // 2% of canvas width
const PLAYER_SIZE = 0.5 / 100; // 2% of canvas width
const PLAYER_COLOR = "#f542bc";

/**
 * Draw a player
 */
export function drawPlayer(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  player: Player
) {
  const realX = player.position[0] * canvas.width;
  const realY = player.position[1] * canvas.height;
  const realFontSize = PLAYER_USERNAME_FONT_SIZE * canvas.width;
  const realRadius = PLAYER_SIZE * canvas.width;

  // Circle
  context.strokeStyle = "white";
  context.lineWidth = 1;
  context.fillStyle = PLAYER_COLOR;
  context.beginPath();
  context.arc(realX, realY, realRadius, 0, FULL_CIRCLE);
  context.fill();
  context.stroke();

  // Username
  context.fillStyle = "white";
  context.font = `${realFontSize}px Arial`;
  context.textAlign = "center";
  context.fillText(player.name, realX, realY - realRadius / 2 - realFontSize);
}

/**
 * Draw all players
 * @param canvas
 * @param context
 * @param players
 */
export function drawPlayers(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  players: Player[]
) {
  context.reset();
  for (let player of players) {
    drawPlayer(canvas, context, player);
  }
}
