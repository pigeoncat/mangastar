import request from '../utils/request'

export function getAuthorStatus() {
    return request.get('/author/status');
}

export function register(params) {
    return request.post('/author/register', params);
}

export function listComics(params) {
    return request.get('/author/comics', { params });
}

export function publishComic(params) {
    return request.post('/author/comic', params);
}

export function listChapters(comicId, params) {
    return request.get(`/author/comic/chapters/${comicId}`, { params });
}

export function publishChapter(comicId,params) {
    return request.post(`/author/comic/chapter/${comicId}`, params);
}

export function deleteChapter(id) {
    return request.delete(`/author/comic/chapter/${id}`);
}

export function getChapter(id) {
    return request.get(`/author/comic/chapter/${id}`);
}

export function updateChapter(id,params) {
    return request.put(`/author/comic/chapter/${id}`,params);
}

