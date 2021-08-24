import {
  login
} from '@/api/login'
import {
  getToken,
  setToken,
  removeToken
} from '@/utils/token'

const user = {
  state: {
    user: '',
    username: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    avatar: '',
    introduction: '',
    roles: [],
    menus: undefined,
    elements: undefined,
    setting: {
      articlePlatform: []
    },
    tenantId: '',
    type: '',
    id: undefined,
    sysStatus: 0,
    tokenOverdueFlag: false
  },

  mutations: {
    SET_TYPE: (state, type) => {
      state.type = type
    },
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_USERNAME: (state, username) => {
      state.username = username
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_MENUS: (state, menus) => {
      state.menus = menus
    },
    SET_ELEMENTS: (state, elements) => {
      state.elements = elements
    },
    SET_TENANT_ID: (state, tenantId) => {
      state.tenantId = tenantId
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    LOGIN_SUCCESS: () => {
      console.log('login success')
    },
    LOGOUT_USER: state => {
      state.user = ''
    },
    SET_STS_STATUS: (state, sysStatus) => {
      state.sysStatus = sysStatus
    },
    SET_TOKEN_OVERDUE_FLAG: (state, status) => {
      state.tokenOverdueFlag = status
    }
  },

  actions: {
    Login ({
      commit
    }, userInfo) {
      const username = userInfo.userName.trim()
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_MENUS', undefined)
      commit('SET_ELEMENTS', undefined)
      commit('SET_USERNAME', username)
      removeToken()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          let token = response === undefined ? '' : response.token
          setToken(token)
          commit('SET_TOKEN', token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
