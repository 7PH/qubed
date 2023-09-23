# Qubed

Qubed is a contamination game, made during Qubee Dev Challenge's second edition by a team of 6 during 2 days.

## Rules 

The game is played using arrow keys. Everyone is represented by a circle on a 2D canvas and can freely move. When the game starts, one "infected" player is randomly chosen. His goal is to contaminate all other players, while other players need to run away. Once a player gets contaminated, he can himself contaminate others. The game ends when everyone is infected, and a score based on the survival time and number of people infected is computed for each player.

## Stack

Qubed is implemented using Java in the backend, and Vue3/TypeScript in the frontend. Communication is made using websockets.
