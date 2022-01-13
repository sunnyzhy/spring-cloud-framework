import {
  login,
  loginByOAuth2,
  logout
} from '@/api/login'

import {
  getToken,
  setToken,
  removeToken
} from '@/utils/token'

const server = {
  state: {
    username: '',
    token: getToken()
  },

  mutations: {
    SET_USERNAME: (state, username) => {
      state.username = username
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    }
  },

  actions: {
    login ({
      commit
    }, user) {
      commit('SET_USERNAME', '')
      commit('SET_TOKEN', '')
      return new Promise((resolve, reject) => {
        login(user).then(response => {
          commit('SET_USERNAME', user.userName)
          commit('SET_TOKEN', response.token)
          setToken(response.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    refresh ({ commit }) {

    },
    logout ({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        if (state.token === '') {
          return resolve()
        }
        logout(state.token).then(() => {
          commit('SET_USERNAME', '')
          commit('SET_TOKEN', '')
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    removeToken ({commit}) {
      commit('SET_USERNAME', '')
      commit('SET_TOKEN', '')
      removeToken()
    },
    loginByOAuth2 ({
      commit
    }, user) {
      commit('SET_USERNAME', '')
      commit('SET_TOKEN', '')
      return new Promise((resolve, reject) => {
        loginByOAuth2(user).then(response => {
          commit('SET_USERNAME', user.userName)
          commit('SET_TOKEN', response.token)
          setToken(response.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default server
