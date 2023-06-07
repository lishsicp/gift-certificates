import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
  BASE_URL,
  CLIENT_ID,
  CLIENT_SECRET,
  REDIRECT_URI,
  TOKEN_PATH,
} from "../authorizeEndpoint";

export const buildQueryString = (paramsObj) => {
  return Object.keys(paramsObj)
  .map(
      (key) =>
          `${encodeURIComponent(key)}=${encodeURIComponent(paramsObj[key])}`
  )
  .join("&");
};

const baseQuery = fetchBaseQuery({
  baseUrl: BASE_URL,
  credentials: "include",
});

export const tokenApiSlice = createApi({
  reducerPath: "tokenApiSlice",
  baseQuery: baseQuery,
  endpoints: (builder) => ({
    token: builder.mutation({
      query: (code) => {
        const queryString = buildQueryString({
          code,
          grant_type: "authorization_code",
          redirect_uri: REDIRECT_URI,
          client_id: CLIENT_ID,
        });

        const formData = new FormData();
        formData.append("client_secret", CLIENT_SECRET);

        return {
          url: `${TOKEN_PATH}?${queryString}`,
          method: "POST",
          body: formData,
        };
      },
    }),
  }),
});

export const { useTokenMutation } = tokenApiSlice;

