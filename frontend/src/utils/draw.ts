import { GameObject, Player, PlayerHealth } from "../types";
import { ImagesLoaded } from "./image";
import { sortPlayers } from "./score";

const FULL_CIRCLE = 2 * Math.PI;

const BOUNDARY_COLOR = "pink";
const BOUNDARY_WIDTH = 4;

const PLAYER_USERNAME_FONT_SIZE = 1.3 / 100; // % of canvas width
const PLAYER_SIZE = 1.5 / 100; // % of canvas width

const PLAYER_FILL_COLOR = {
  [PlayerHealth.Healthy]: "#78eb7b",
  [PlayerHealth.Infected]: "#c3d32e",
  [PlayerHealth.Contagious]: "#f26179",
};

const PLAYER_STROKE_COLOR = {
  [PlayerHealth.Healthy]: "#169119",
  [PlayerHealth.Infected]: "#e0840b",
  [PlayerHealth.Contagious]: "#bf1531",
};

const PLAYER_INFECTED_SHAKE_AMPLITUDE = 0.1 / 100; // % of canvas width

/**
 * Draw a player circle
 */
export function drawPlayerCircle(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  player: Player
) {
  let realX = player.x * canvas.width;
  let realY = player.y * canvas.height;
  const realRadius = PLAYER_SIZE * canvas.width;

  if (player.health !== PlayerHealth.Healthy) {
    realX +=
      canvas.height * PLAYER_INFECTED_SHAKE_AMPLITUDE * (Math.random() * 2 - 1);
    realY +=
      canvas.height * PLAYER_INFECTED_SHAKE_AMPLITUDE * (Math.random() * 2 - 1);
  }

  context.strokeStyle = PLAYER_STROKE_COLOR[player.health];
  context.lineWidth = 1;
  context.fillStyle = PLAYER_FILL_COLOR[player.health];
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

  context.fillStyle = PLAYER_FILL_COLOR[player.health];
  context.font = `${realFontSize}px Arial`;
  context.textAlign = "center";
  context.fillText(
    `${isLocalPlayer ? "⭐" : ""}${player.name}`,
    realX,
    realY - realRadius / 2 - realFontSize
  );
}

/**
 * Draw all players
 */
export function drawPlayers(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  gameObject: GameObject
) {
  gameObject.players.forEach((player) =>
    drawPlayerCircle(canvas, context, player)
  );
  // Draw usernames afterwards so they are on top of circles
  gameObject.players.forEach((player) =>
    drawPlayerUsername(
      canvas,
      context,
      player,
      player.id === gameObject.playerId
    )
  );
}

/**
 * Draw a bush
 */
export function drawElement(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  image: HTMLImageElement,
  size: number,
  x: number,
  y: number,
  name?: string
) {
  const realX = x * canvas.width;
  const realY = y * canvas.height;

  const aspectRatio = image.width / image.height;
  const bushWidth = size * canvas.width;
  const bushHeight = bushWidth / aspectRatio;
  context.drawImage(
    image,
    realX - bushWidth / 2,
    realY - bushHeight,
    bushWidth,
    bushHeight
  );

  if (name) {
    context.fillStyle = "#fff";
    context.font = "bold " + Math.floor(bushHeight) * 0.3 + "px Arial";
    context.textAlign = "center";
    context.fillText(name, realX, realY - bushHeight * 1.3);
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

export function drawGameFinished(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  winner: string
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
  context.fillText(
    `Winner: ${winner}`,
    canvas.width / 2,
    canvas.height / 2 + 30
  );
}

/**
 * Draw whole game
 */
export function drawGame(
  canvas: HTMLCanvasElement,
  context: CanvasRenderingContext2D,
  gameObject: GameObject,
  initialized: boolean,
  images: ImagesLoaded
) {
  if (!initialized) {
    return;
  }

  drawPlayers(canvas, context, gameObject);
  drawElement(canvas, context, images.bush2, 0.2, 0.2, 1, "George");
  drawElement(canvas, context, images.bush2, 0.2, 0.6, 1, "George");
  drawElement(canvas, context, images.cloud, 0.4, 0.5, 0.3);

  drawGameBoundaries(canvas, context);

  if (gameObject.gameFinished) {
    const winner = sortPlayers(gameObject.players)[0];
    drawGameFinished(canvas, context, winner.name);
  }
}
