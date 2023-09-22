import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { GameObject, GameState, Player } from "../types";

const GAME_END_DELAY = 5000;

const PORT =
  document.location.hostname === "localhost" ? 8080 : document.location.port;

const WS_PROTOCOL = document.location.protocol === "https:" ? "wss" : "ws";

enum OutboundMessageType {
  Start = "start",
  Commands = "commands",
  Ready = "ready",
}

enum InboundMessageType {
  StateUpdate = "GAME_STATE",
  Connected = "CONNECTED",
}

export let gameState: GameObject = {
  playerId: 0,
  players: [],
  gameFinished: false,
};

export let connectionReference: WebSocket | undefined = undefined;
export let sendCommands: <T>(content: T) => void;
export let closeConnection: () => void;

export function useWebSocket() {
  const connection = ref<WebSocket | undefined>(undefined);
  const playerList = ref<Player[]>([]);
  const playerId = ref<number>(0);

  const route = useRoute();
  const router = useRouter();
  const playername = route.query.name ?? "incognito";

  function connect() {
    const socket = new WebSocket(
      `${WS_PROTOCOL}://${document.location.hostname}:${PORT}/websocket/${playername}`
    );
    connection.value = socket;
    connectionReference = socket;

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
  }

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
    } else if (data.gameState === GameState.FINISHED) {
      gameState.gameFinished = true;
      gameState.players = data.players;

      setTimeout(() => {
        router.push(`/scoreboard?name=${playername}`);
        gameState.gameFinished = false;
      }, GAME_END_DELAY);
    } else if (data.gameState === GameState.RUNNING) {
      gameState.players = data.players;

      if (route.path.includes("waiting")) {
        router.push("/game");
      }
    }
  }

  function handleConnected(data: { id: number }) {
    playerId.value = data.id;
    gameState.playerId = data.id;
  }

  function startGame() {
    connection.value?.send(
      JSON.stringify({ type: OutboundMessageType.Start, content: {} })
    );
  }

  function setReady() {
    connection.value?.send(
      JSON.stringify({
        type: OutboundMessageType.Ready,
        content: {},
      })
    );
  }

  function disconnect() {
    if (connectionReference?.OPEN) {
      connectionReference.close();
    }
  }

  return { playerId, playerList, connect, disconnect, setReady, startGame };
}
