import request from '../utils/request'

export function listHomeComics() {
    return request.get('/front/home/comics');
}


export function listHomeFriendLinks() {
    return request.get('/front/home/friend_Link/list');
}

