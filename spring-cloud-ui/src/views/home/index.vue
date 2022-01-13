<template>
  <div>
    <h1>Welcome</h1>
    <el-button type='primary' size='small' @click='doLogout'>退出</el-button>
  </div>
</template>

<script>
export default {
  name: 'home',
  data () {
    return {
    }
  },
  methods: {
    doLogout () {
      this.$store.dispatch('logout').then(() => {
        location.reload()
      })
    }
  }
}
</script>

<style scoped>

</style>
