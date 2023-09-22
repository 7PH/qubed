import { intervalToDuration } from "date-fns";
import formatDuration from "date-fns/formatDuration";
import { Player } from "../types";

export function formatSurvivalTime(ms: number) {
  return formatDuration(intervalToDuration({ start: 0, end: Math.round(ms) }));
}

export function sortPlayers(players: Player[]) {
  return players
    .slice(0)
    .sort(
      (p1, p2) => p2.playerStats.survivalTime - p1.playerStats.survivalTime
    );
}
