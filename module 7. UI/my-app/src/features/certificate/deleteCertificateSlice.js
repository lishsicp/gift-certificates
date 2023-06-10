import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  loading: false,
  errors: [],
  isSuccess: false,
};

const deleteCertificateSlice = createSlice({
  name: "delete",
  initialState,
  reducers: {
    deleteCertificateRequest: (state) => {
      state.loading = true;
      state.isSuccess = false;
      state.errors = [];
    },
    deleteCertificateSuccess: (state) => {
      state.loading = false;
      state.isSuccess = true;
      state.errors = [];
    },
    deleteCertificateFailure: (state, action) => {
      state.loading = false;
      state.isSuccess = false;
      state.errors = action.payload;
    },
    resetDeletionState: (state) => {
      state.loading = initialState.loading;
      state.isSuccess = initialState.isSuccess;
      state.errors = initialState.errors;
    },
  },
});

export default deleteCertificateSlice.reducer;

export const {
  deleteCertificateRequest,
  deleteCertificateSuccess,
  deleteCertificateFailure,
  resetDeletionState,
} = deleteCertificateSlice.actions;
