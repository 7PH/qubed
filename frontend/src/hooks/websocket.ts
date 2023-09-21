import { onMounted, onUnmounted, ref } from "vue";
import { useRoute } from "vue-router";
import { GameState, Player } from "../types";

const PORT = 8080;

enum OutboundMessageType {
  Start = "start",
  Commands = "commands",
}

enum InboundMessageType {
  StateUpdate = "game_state",
}

export let gameState: Player[];
export let sendCommands: <T>(content: T) => void;

export function useWebSocket() {
  const connection = ref<WebSocket | undefined>(undefined);
  const playerList = ref<Player[]>([]);

  const route = useRoute();

  onMounted(() => {
    const playername = route.query.name ?? "incognito";

    const socket = new WebSocket(
      `ws://localhost:${PORT}/websocket/${playername}`
    );
    connection.value = socket;

    // socket.addEventListener("open", (_event) => {
    //   socket.send("Hello Server!");
    // });

    // Listen for messages
    socket.addEventListener("message", (event) => {
      console.log("Message from server ", event.data);
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
  });

  onUnmounted(() => {
    connection.value?.close();
  });

  function handleMessage(data: any) {
    handleStateUpdate(data);
    // switch (data.type) {
    //   case InboundMessageType.StateUpdate:
    //     handleStateUpdate(data.content);
    //     break;
    //   default:
    //     console.warn(`Unknown message type received: ${data.type}`);
    // }
  }

  function handleStateUpdate(data: {
    gameState: GameState;
    players: Player[];
  }) {
    if (data.gameState === GameState.PENDING) {
      playerList.value = data.players;
    } else {
      gameState = playerList.value;
    }
  }

  function startGame() {
    connection.value?.send(
      JSON.stringify({ type: OutboundMessageType.Start, content: "" })
    );
  }

  return { playerList, startGame };
}
