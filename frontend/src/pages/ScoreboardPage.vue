<script lang="ts" setup>
const mockedUsers = Array.from({ length: 20}).map((_, index) => ({
  name: `Player${index +1}`,
  place: index + 1
}))
const winner = 'Coolguy';
</script>

<template>
  <div class="layout">
    <div class="winner">
      <h2>Congrats {{winner.toUpperCase()}}, <br/> you won!!!1! </h2>
      <iframe src="https://giphy.com/embed/2gtoSIzdrSMFO" width="120" height="90" frameBorder="0" ></iframe>
    </div>
    <div class="scoreboard">
        <div class="row">
          <div class="column">
            <strong>Name</strong>
          </div>
          <div class="column">
            <strong>Place</strong>
          </div>
        </div>

        <div class="row" v-for="(user) in mockedUsers">
          <div class="column">{{user.name}}</div>
          <div class="column">{{user.place}}</div>
        </div>
    </div>

    <button class="wait-btn">Waiting room</button>
  </div>
</template>

<style scoped>

.winner {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-bottom: 20px;
}
.scoreboard {
  overflow: auto;
  border: 1px solid pink;
  border-radius: 4px;
  height: 70vb;
}

.row {
  display: flex;
  padding: 10px;
  border-bottom: 1px solid pink;
}
.column {
  flex-grow: 1;
}

.wait-btn {
  margin-top: 10px;
  width: 100%;
}
</style>