import {createSlice} from "@reduxjs/toolkit";

const initialState = {
  certificates: [], loading: false, errors: [], page: null,
};

const fetchCertificatesSlice = createSlice({
  name: "fetch", initialState, reducers: {
    fetchCertificatesRequest: (state) => {
      state.loading = true;
    }, fetchCertificatesSuccess: (state, action) => {
      state.certificates = action.payload;
      state.loading = false;
      state.page = action.payload.page;
      state.errors = null;
    }, fetchCertificatesFailure: (state, action) => {
      state.errors = action.payload;
      state.loading = false;
    },
  }
})

export default fetchCertificatesSlice.reducer;
export const {
  fetchCertificatesRequest,
  fetchCertificatesSuccess,
  fetchCertificatesFailure
} = fetchCertificatesSlice.actions;