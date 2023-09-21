
export type Player = {
    id: number;
    name: string;
    ready: boolean;
    infected: boolean;
    position: [number, number]
}

export enum GameState {
    PENDING = 'pending',
    RUNNING = 'running'
}