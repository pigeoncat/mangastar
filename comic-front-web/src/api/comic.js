import request from '../utils/request'

export function listCategorys(params) {
    return request.get('/front/comic/category/list', { params });
}

export function searchComics(params) {
    return request.get('/front/search/comics', { params });
}

export function getComicById(comicId) {
    return request.get(`/front/comic/${comicId}`);
}

export function addVisitCount(params) {
    return request.post('/front/comic/visit', params);
}

export function getLastChapterAbout(params) {
    return request.get('/front/comic/last_chapter/about', { params });
}

export function listRecComics(params) {
    return request.get('/front/comic/rec_list', { params });
}

export function getFirstChapter(params) {
    return request.get('/front/comic/chapter/first', { params });
}

export function listChapters(params) {
    return request.get('/front/comic/chapter/list', { params });
}

export function getComicContent(chapterId) {
    return request.get(`/front/comic/content/${chapterId}`);
}

export function getPreChapterId(chapterId) {
    return request.get(`/front/comic/pre_chapter_id/${chapterId}`);
}

export function getNextChapterId(chapterId) {
    return request.get(`/front/comic/next_chapter_id/${chapterId}`);
}

export function listVisitRankComics() {
    return request.get('/front/comic/visit_rank');
}

export function listNewestRankComics() {
    return request.get('/front/comic/newest_rank');
}

export function listUpdateRankComics() {
    return request.get('/front/comic/update_rank');
}

export function listNewestComments(params) {
    return request.get('/front/comic/comment/newest_list',{ params });
}

