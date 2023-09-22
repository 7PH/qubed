import { GameObject, Player } from "../types";

const FULL_CIRCLE = 2 * Math.PI;

const BOUNDARY_COLOR = "pink";
const BOUNDARY_WIDTH = 4;

const PLAYER_USERNAME_FONT_SIZE = 1.8 / 100; // % of canvas width
const PLAYER_SIZE = 2 / 100; // % of canvas width
const PLAYER_SANE_FILL_COLOR = "#78eb7b";
const PLAYER_SANE_STROKE_COLOR = "#169119";
const PLAYER_INFECTED_FILL_COLOR = "#f26179";
const PLAYER_INFECTED_STROKE_COLOR = "#bf1531";
const PLAYER_INFECTED_SHAKE_AMPLITUDE = 0.1 / 100; // % of canvas width

/**
 * Draw a player circle
 */
export function drawPlayerCircle(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  player: Player,
) {
  let realX = player.x * canvas.width;
  let realY = player.y * canvas.height;
  const realRadius = PLAYER_SIZE * canvas.width;

  if (player.infected) {
    realX += canvas.height * PLAYER_INFECTED_SHAKE_AMPLITUDE * (Math.random() * 2 - 1);
    realY += canvas.height * PLAYER_INFECTED_SHAKE_AMPLITUDE * (Math.random() * 2 - 1);
  }

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
}

/**
 * Draw a player username
 */
export function drawPlayerUsername(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  player: Player,
  isLocalPlayer: boolean
) {
  const realX = player.x * canvas.width;
  const realY = player.y * canvas.height;
  const realFontSize = PLAYER_USERNAME_FONT_SIZE * canvas.width;
  const realRadius = PLAYER_SIZE * canvas.width;

  context.fillStyle = player.infected
    ? PLAYER_INFECTED_FILL_COLOR
    : PLAYER_SANE_FILL_COLOR;
  context.font = `${realFontSize}px Arial`;
  context.textAlign = "center";
  context.fillText(`${isLocalPlayer ? '⭐' : ''}${player.name}`, realX, realY - realRadius / 2 - realFontSize);
}

/**
 * Draw all players
 */
export function drawPlayers(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  gameObject: GameObject
) {
  gameObject.players.forEach(player => drawPlayerCircle(canvas, context, player));
  // Draw usernames afterwards so they are on top of circles
  gameObject.players.forEach(player => drawPlayerUsername(canvas, context, player, player.id === gameObject.playerId));
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

export function drawGameFinished(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
) {
  context.fillStyle = "rgba(0, 0, 0, 0.5)";
  context.fillRect(0, 0, canvas.width, canvas.height);

  context.fillStyle = "white";
  context.font = "bold 2rem Arial";
  context.textAlign = "center";
  context.fillText("Game finished!", canvas.width / 2, canvas.height / 2);

  context.fillStyle = "white";
  context.font = "bold 1.5rem Arial";
  context.textAlign = "center";
  context.fillText(`Winner: ${'????'}`, canvas.width / 2, canvas.height / 2 + 30);
}

/**
 * Draw whole game
 */
export function drawGame(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  gameObject: GameObject
) {
  drawGameBoundaries(canvas, context);
  drawPlayers(canvas, context, gameObject);
  if (gameObject.gameFinished) {
    drawGameFinished(canvas, context);
  }
}
