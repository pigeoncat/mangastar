
import { createStore } from 'vuex'

const debug = process.env.NODE_ENV !== 'production'

export default createStore({
    state() {
        return {
            allChapters: []
        };
    },
    mutations: {
        setAllChapters(state, chapters) {
            state.allChapters = chapters;
        }
    },
    actions: {
        fetchAllChapters({ commit }, chapters) {
            commit('setAllChapters', chapters);
        }
    },
    getters: {
        getAllChapters(state) {
            return state.allChapters;
        }
    }
})