import { GameObject, Player } from "../types";

const FULL_CIRCLE = 2 * Math.PI;

const BOUNDARY_COLOR = "#aed4eb";
const BOUNDARY_WIDTH = 4;

const PLAYER_USERNAME_FONT_SIZE = 1.8 / 100; // % of canvas width
const PLAYER_SIZE = 1.5 / 100; // % of canvas width
const PLAYER_SANE_FILL_COLOR = "#78eb7b";
const PLAYER_SANE_STROKE_COLOR = "#169119";
const PLAYER_INFECTED_FILL_COLOR = "#f26179";
const PLAYER_INFECTED_STROKE_COLOR = "#bf1531";

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
  context.strokeStyle = player.infected
    ? PLAYER_INFECTED_STROKE_COLOR
    : PLAYER_SANE_STROKE_COLOR;
  context.lineWidth = 1;
  context.fillStyle = player.infected
    ? PLAYER_INFECTED_FILL_COLOR
    : PLAYER_SANE_FILL_COLOR;
  context.beginPath();
  context.arc(realX, realY, realRadius, 0, FULL_CIRCLE);
  context.fill();
  context.stroke();

  // Username
  context.fillStyle = player.infected
    ? PLAYER_INFECTED_FILL_COLOR
    : PLAYER_SANE_FILL_COLOR;
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
  gameState: GameObject
) {
  drawGameBoundaries(canvas, context);
  drawPlayers(canvas, context, gameState.players);
}
