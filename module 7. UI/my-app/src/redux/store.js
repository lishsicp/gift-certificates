import {
  configureStore, combineReducers, applyMiddleware,
} from "@reduxjs/toolkit";
import {certificatesReducer} from "./GiftCertificatesReducer";
import searchParamsReducer from "./SearchParamsReducer";
import {
  createStateSyncMiddleware, initMessageListener,
} from "redux-state-sync";
import thunk from "redux-thunk";

const root = combineReducers({
  data: certificatesReducer, search: searchParamsReducer,
});

const middleware = [thunk, createStateSyncMiddleware()];

const store = configureStore({
  reducer: root, middleware: middleware,
});

applyMiddleware(...middleware);

initMessageListener(store);

export default store;


