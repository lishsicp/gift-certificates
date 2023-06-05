import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
  updateAccessToken,
  resetAuthState,
} from "../features/auth/authTokenSlice";
import {
  BASE_URL,
  CLIENT_ID,
  CLIENT_SECRET,
  TOKEN_PATH,
} from "../authorizeEndpoint";
import { API_ROOT } from "../constants";

const baseQuery = fetchBaseQuery({
  baseUrl: API_ROOT,
  prepareHeaders: (headers) => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken && !isTokenExpired(accessToken)) {
      headers.set("Authorization", `Bearer ${accessToken}`);
      headers.set("Content-Type", "application/json");
    }
    return headers;
  },
});

function isTokenExpired(token) {
  const tokenData = JSON.parse(atob(token.split(".")[1]));
  const expirationTime = tokenData.exp * 1000;
  const currentTime = Date.now();
  return currentTime > expirationTime;
}

const baseQueryWithReauth = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);
  if (result?.error?.status === 401) {
    // trying to sent refresh token request when access denied
    console.debug("sending refresh token. Response: " + JSON.stringify(result));
    const refreshToken =
      api.getState().auth.refreshToken || localStorage.getItem("refreshToken");

    const formData = new FormData();
    formData.append("client_secret", CLIENT_SECRET);
    formData.append("client_id", CLIENT_ID);
    formData.append("grant_type", "refresh_token");
    formData.append("refresh_token", refreshToken);

    const refreshResult = await baseQuery(
      {
        url: `${BASE_URL}${TOKEN_PATH}`,
        method: "POST",
        body: formData,
        headers: {
          "Access-Control-Allow-Origin": "*",
          Host: BASE_URL,
        },
        credentials: "include",
      },
      api,
      extraOptions
    );
    console.debug("Refresh request response: " + JSON.stringify(refreshResult));
    if (refreshResult?.data) {
      api.dispatch(updateAccessToken(refreshResult.data));
      // continue with default flow
      result = await baseQuery(args, api, extraOptions);
    } else {
      // logout if refresh request fails as well
      api.dispatch(resetAuthState());
    }
  }

  return result;
};

export const protecredApiSlice = createApi({
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({}),
});
