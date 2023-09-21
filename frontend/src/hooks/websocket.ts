import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { GameState, Player } from "../types";

const PORT = 8080;

enum OutboundMessageType {
  Start = "start",
  Commands = "commands",
  Ready = "ready",
}

enum InboundMessageType {
  StateUpdate = "GAME_STATE",
  Connected = "CONNECTED",
}

export let gameState: { playerId: number; players: Player[] } = {
  playerId: 0,
  players: [],
};
export let sendCommands: <T>(content: T) => void;
export let closeConnection: () => void;

export function useWebSocket() {
  const connection = ref<WebSocket | undefined>(undefined);
  const playerList = ref<Player[]>([]);
  const playerId = ref<number>(0);

  const route = useRoute();

  onMounted(() => {
    const playername = route.query.name ?? "incognito";

    const socket = new WebSocket(
      `ws://localhost:${PORT}/websocket/${playername}`
    );
    connection.value = socket;

    // Listen for messages
    socket.addEventListener("message", (event) => {
      handleMessage(JSON.parse(event.data));
    });

    socket.addEventListener("error", (error) => {
      console.error(error);
    });

    socket.addEventListener("close", () => {
      console.warn("connection closed");
    });

    sendCommands = function <T>(content: T) {
      socket.send(
        JSON.stringify({
          type: OutboundMessageType.Commands,
          content,
        })
      );
    };

    closeConnection = function () {
      socket.close();
    };
  });

  function handleMessage(data: any) {
    switch (data.type) {
      case InboundMessageType.StateUpdate:
        handleStateUpdate(data.content);
        break;
      case InboundMessageType.Connected:
        handleConnected(data.content);
        break;
      default:
        console.warn(`Unknown message type received: ${data.type}`);
    }
  }

  function handleStateUpdate(data: {
    gameState: GameState;
    players: Player[];
  }) {
    if (data.gameState === GameState.PENDING) {
      playerList.value = data.players;
    } else {
      gameState.players = playerList.value;
    }
  }

  function handleConnected(data: { id: number }) {
    playerId.value = data.id;
    gameState.playerId = data.id;
  }

  function startGame() {
    connection.value?.send(
      JSON.stringify({ type: OutboundMessageType.Start, content: "" })
    );
  }

  function setReady() {
    connection.value?.send(
      JSON.stringify({
        type: OutboundMessageType.Ready,
        content: "",
      })
    );
  }

  return { playerId, playerList, setReady, startGame };
}
