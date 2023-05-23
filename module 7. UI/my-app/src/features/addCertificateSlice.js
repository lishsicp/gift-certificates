import {createSlice} from "@reduxjs/toolkit";

const initialState = {
  certificate: [], loading: false, errors: [], isSuccess: false
};

const addCertificateSlice = createSlice({
  name: "add", initialState, reducers: {
    addCertificateRequest: (state) => {
      state.loading = true;
      state.isSuccess = false;
    }, 
    addCertificateSuccess: (state, action) => {
      state.certificate = action.payload;
      state.loading = false;
      state.errors = null;
      state.isSuccess = true;
    }, 
    addCertificateFailure: (state, action) => {
      state.errors = action.payload;
      state.loading = false;
      state.isSuccess = false;
    }, 
    resetCertificateState: (state) => {
      state.certificate = initialState.certificate;
      state.loading = initialState.loading;
      state.errors = initialState.errors;
      state.isSuccess = initialState.isSuccess;
    }
  }
})

export default addCertificateSlice.reducer;
export const {
    addCertificateRequest,
    addCertificateSuccess,
    addCertificateFailure,
    resetCertificateState
} = addCertificateSlice.actions;