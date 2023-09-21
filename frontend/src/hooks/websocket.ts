import { ref, onMounted, onUnmounted } from "vue";
import { GameState, Player } from "../types";

const PORT = 3000;

enum OutboundMessageType {
  Start = "start",
  Commands = "commands",
}

enum InboundMessageType {
  StateUpdate = "game_state",
}

export let gameState: any;
export let sendCommands: any;

export function useWebSocket() {
  const connection = ref<WebSocket | undefined>(undefined);
  const playerList = ref<Player[]>([]);

  onMounted(() => {
    const socket = new WebSocket(`ws://localhost:${PORT}`);
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

    sendCommands = function (content: string) {
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
    switch (data.type) {
      case InboundMessageType.StateUpdate:
        handleStateUpdate(data.content);
        break;
      default:
        console.warn(`Unknown message type received: ${data.type}`);
    }
  }

  function handleStateUpdate(data: {
    game_state: GameState;
    players: Player[];
  }) {
    if (data.game_state === GameState.PENDING) {
      playerList.value = data.players;
    } else {
      gameState = playerList;
    }
  }

  function startGame() {
    connection.value?.send(
      JSON.stringify({ type: OutboundMessageType.Start, content: "" })
    );
  }

  return { playerList, startGame };
}
