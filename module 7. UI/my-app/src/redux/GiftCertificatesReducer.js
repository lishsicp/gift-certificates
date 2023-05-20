const initialState = {
  certificates: [], loading: false, errors: [], page: null,
};

const cases = {
  FETCH_CERTIFICATES_REQUEST: (state, action) => Object.assign({}, state, {
    ...state, loading: true,
  }), FETCH_CERTIFICATES_SUCCESS: (state, action) => Object.assign({}, state, {
    ...state,
    certificates: action.payload,
    loading: false,
    page: action.payload.page,
    errors: []
  }), FETCH_CERTIFICATES_FAILURE: (state, action) => Object.assign({}, state, {
    ...state, loading: false, errors: action.payload
  }),
};

export const certificatesReducer = (state = initialState, action) => {
  const handler = cases[action.type];
  return handler ? handler(state, action) : state;
};
