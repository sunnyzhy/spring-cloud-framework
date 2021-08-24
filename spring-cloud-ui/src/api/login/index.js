import ajax from '@/utils/ajax'

export function login (userName, password) {
  const data = {
    userName,
    password
  }
  return ajax({
    url: 'aut/login',
    method: 'post',
    data
  })
}
