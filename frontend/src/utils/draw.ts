import { Player } from "../types";

const FULL_CIRCLE = 2 * Math.PI;

const BOUNDARY_COLOR = "#f542bc";
const BOUNDARY_WIDTH = 4;

const PLAYER_USERNAME_FONT_SIZE = 1.5 / 100; // % of canvas width
const PLAYER_SIZE = 1 / 100; // % of canvas width
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
 */
export function drawPlayers(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  players: Player[]
) {
  for (let player of players) {
    drawPlayer(canvas, context, player);
  }
}

/**
 * Draw game boundaries
 */
export function drawGameBoundaries(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D
) {
  context.strokeStyle = BOUNDARY_COLOR;
  context.lineWidth = BOUNDARY_WIDTH;
  context.strokeRect(0, 0, canvas.width, canvas.height);
}

/**
 * Draw whole game
 */
export function drawGame(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  players: Player[]
) {
  drawGameBoundaries(canvas, context);
  drawPlayers(canvas, context, players);
}
