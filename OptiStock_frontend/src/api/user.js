import request from '@/utils/request'
// 登录
export function userLogin(username, password) {
    return request({
        url: '/user/login',
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
            isToken: false
          },
        data: { username, password }
    })
}

export function userRegister(username, nickname, password) {
    return request({
        url: '/user/register',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: { // 这里应该是 `data` 而不是 `params`
            username,
            nickname,
            password
        }
    })
}

//更新用户信息
export function userUpdateInfo(userInfo) {
    return request({
        url: '/user/updateInfo',
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
            isToken: true
        },
        data: userInfo
    })
}

//修改密码
export function changePassword(oldPassword, newPassword) {
    return request({
        url: '/user/changePassword',
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
            isToken: true
        },
        data: {oldPassword, newPassword}
    })
}

// export function logout() {
//     return request({
//         url: '/logout',
//         method: 'post'
//     })
// }

// export function getUserInfo(userId) {
//     return request ({
//         url: '/user/userInfo',
//         method: 'get',
//         params: {"userId":userId}
//     })
// }


// export function savaUserInfo(userinfo) {
//     return request({
//         url: '/user/userInfo',
//         method: 'put',
//         data: userinfo
//     })
// }