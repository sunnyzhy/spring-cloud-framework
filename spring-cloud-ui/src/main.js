// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import i18n from './lang'
import ElementUI from 'element-ui'
import JsonViewer from 'vue-json-viewer'
import 'element-ui/lib/theme-chalk/index.css'
import { getToken } from './utils/token'

Vue.config.productionTip = false

Vue.use(ElementUI, {
  size: 'medium',
  i18n: (key, value) => i18n.t(key, value)
})

Vue.use(JsonViewer)

const whiteList = ['/login', '/auth', '/favicon.ico']

router.beforeEach((to, from, next) => {
  if (getToken()) {
    if (to.path === '/login' || to.path === '/auth') {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
