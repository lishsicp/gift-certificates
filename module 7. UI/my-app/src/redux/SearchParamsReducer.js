const initialState = {
  page: 1,
  size: 10,
  name: null,
  description: null,
  tags: [],
  date_sort: "asc",
  name_sort: null,
};

const cases = {
  RESET_SEARCH_PARAMS: (state, action) => Object.assign({}, state, {
    ...state,
    page: initialState.page,
    size: initialState.size,
    name: initialState.name,
    description: initialState.description,
    tags: initialState.tags,
    date_sort: initialState.date_sort,
    name_sort: initialState.name_sort,
  }), UPDATE_SEARCH_PARAMS: (state, action) => Object.assign({}, state, {
    ...state,
    page: action.payload.page,
    size: action.payload.size,
    name: action.payload.name,
    description: action.payload.description,
    tags: action.payload.tags,
    date_sort: action.payload.date_sort,
    name_sort: action.payload.name_sort,
  }), UPDATE_SIZE_PARAM: (state, action) => Object.assign({}, state, {
    ...state, size: action.payload,
  }),
};

export default function searchParamsReducer(state = initialState, action) {
  const handler = cases[action.type];
  return handler ? handler(state, action) : state;
}
