import { intervalToDuration } from "date-fns";
import formatDuration from "date-fns/formatDuration";

export function formatSurvivalTime(ms: number) {
  return formatDuration(
    intervalToDuration({ start: 0, end: Math.round(ms / 1000) })
  );
}
