import fetch from '@/utils/fetch'
import fetchByOAuth2 from '@/utils/fetch_oauth2'

export function login (user) {
  return fetch({
    url: '/auth/login',
    method: 'post',
    data: {
      username: user.userName,
      password: user.password
    }
  })
}

export function logout (user) {
  return fetch({
    url: '/auth/login',
    method: 'post',
    data: {
      username: user.userName,
      password: user.password
    }
  })
}

export function loginByOAuth2 (user) {
  return fetchByOAuth2({
    url: '/oauth/authorize',
    method: 'get',
    params: user
  })
}

// export function login (user) {
//   const data = {
//     username: user.userName,
//     password: user.password
//   }
//   return fetch({
//     url: '/login',
//     method: 'post',
//     data,
//     transformRequest: [
//       function (data) {
//         let ret = ''
//         for (let it in data) {
//           ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
//         }
//         ret = ret.substring(0, ret.lastIndexOf('&'))
//         return ret
//       }
//     ],
//     headers: {
//       'Content-Type': 'application/x-www-form-urlencoded'
//     }
//   })
// }
