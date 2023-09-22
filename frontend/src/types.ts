export enum PlayerStatus {
  Waiting = "WAITING",
  Ready = "READY",
}

export enum PlayerHealth {
  Healthy = "HEALTHY",
  Infected = "INFECTED",
  Contagious = "CONTAGIOUS",
}

export type Player = {
  id: number;
  name: string;
  status: PlayerStatus;
  health: PlayerHealth;
  x: number;
  y: number;
};

export enum GameState {
  PENDING = "pending",
  RUNNING = "running",
  FINISHED = "finished",
}

export type GameObject = {
  playerId: number;
  players: Player[];
  gameFinished: boolean;
};
