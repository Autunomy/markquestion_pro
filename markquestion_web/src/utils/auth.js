const TokenKey = 'Authentication'

//获取token
export function getToken() {
  return sessionStorage.getItem(TokenKey)
}

//设置token
export function setToken(token) {
  return sessionStorage.setItem(TokenKey,token)
}

//删除token
export function removeToken() {
  return sessionStorage.removeItem(TokenKey)
}
