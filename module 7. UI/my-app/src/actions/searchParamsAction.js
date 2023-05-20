const RESET_SEARCH_PARAMS = "RESET_SEARCH_PARAMS";
const UPDATE_SEARCH_PARAMS = "UPDATE_SEARCH_PARAMS";
const UPDATE_SIZE_PARAM = "UPDATE_SIZE_PARAM";

export const resetSearchParams = () => {
    return {
        type: RESET_SEARCH_PARAMS,
    }
}

export const updateSearchParams = (searchObject) => {
    return {
        type: UPDATE_SEARCH_PARAMS,
        payload: searchObject
    }
}

export const updateSizeParam = (size) => {
    return {
        type: UPDATE_SIZE_PARAM,
        payload: size
    }
}