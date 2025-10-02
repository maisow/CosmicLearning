<template>
  <div class="home">
    <h1>Cosmic Learning</h1>
  </div>
</template>

<script>
export default {
  name: "HomeView",
};
</script>

<style lang="scss" scoped>
.home {
  background-image: url("../assets/albert-home-page.gif");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  padding-top: 40px;
}

h1 {
  margin-left: 150px;
  color: white;
  font-family: papyrus;
  font-size: 100px;
  margin: 0;
}
</style>
