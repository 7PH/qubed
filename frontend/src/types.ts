export enum PlayerStatus {
  Waiting = "WAITING",
  Ready = "READY",
}

export type Player = {
  id: number;
  name: string;
  status: PlayerStatus;
  infected: boolean;
  position: [number, number];
};

export enum GameState {
  PENDING = "pending",
  RUNNING = "running",
}
