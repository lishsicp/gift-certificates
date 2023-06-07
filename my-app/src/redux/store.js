import { configureStore, combineReducers } from "@reduxjs/toolkit";
import thunk from "redux-thunk";
import searchReducer from "../features/certificate/searchSlice";
import certificatesReducer from "../features/certificate/certificatesSlice";
import addCertificatesReducer from "../features/certificate/addCertificateSlice";
import deleteCertificateReducer from "../features/certificate/deleteCertificateSlice";
import authTokenSlice from "../features/auth/authTokenSlice";
import { tokenApiSlice } from "../api/tokenApiSlice";
import { protecredApiSlice } from "../api/protecredApiSlice";

const root = combineReducers({
  data: certificatesReducer,
  search: searchReducer,
  addCertificate: addCertificatesReducer,
  deleteCertificate: deleteCertificateReducer,
  auth: authTokenSlice,
  [tokenApiSlice.reducerPath]: tokenApiSlice.reducer,
  [protecredApiSlice.reducerPath]: protecredApiSlice.reducer,
});

const store = configureStore({
  reducer: root,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(thunk)
      .concat(tokenApiSlice.middleware)
      .concat(protecredApiSlice.middleware),
  devTools: true,
});

export default store;
