export enum PlayerStatus {
  Waiting = "WAITING",
  Ready = "READY",
}

export type Player = {
  id: number;
  name: string;
  status: PlayerStatus;
  infected: boolean;
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
