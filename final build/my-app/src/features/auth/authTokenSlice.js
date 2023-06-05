import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  accessToken: null,
  refreshToken: null,
  email: null,
  code: null,
  isLoading: false,
  isSuccess: false,
};

const authTokenSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    updateAccessToken: (state, action) => {
      console.log(action.payload);
      const accessToken = action?.payload?.access_token;
      if (accessToken) {
        state.accessToken = accessToken;
        state.refreshToken = action?.payload?.refresh_token;
        const claims = JSON.parse(
          atob(action?.payload?.access_token.split(".")[1])
        );
        state.email = claims.email;
        localStorage.setItem("accessToken", state.accessToken);
        localStorage.setItem("refreshToken", state.refreshToken);
        localStorage.setItem("email", state.email);
      }
    },
    resetAuthState: (state) => {
      state.accessToken = initialState.accessToken;
      state.refreshToken = initialState.refreshToken;
      state.userEmail = initialState.userEmail;
      state.code = initialState.code;
      state.isLoading = false;
      state.isSuccess = false;
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("email");
    },
  },
});

export const { updateAccessToken, resetAuthState } = authTokenSlice.actions;
export const selectCurrentAccessToken = (state) => state.auth.accessToken;
export const selectCurrentUser = (state) => state.auth.email;
export default authTokenSlice.reducer;
