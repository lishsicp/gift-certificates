import {
  configureStore, combineReducers,
} from "@reduxjs/toolkit";
import {
  createStateSyncMiddleware, initStateWithPrevTab
} from "redux-state-sync";
import thunk from "redux-thunk";
import searchReducer from '../features/searchSlice';
import certificatesReducer from '../features/certificatesSlice';

const root = combineReducers({
  data: certificatesReducer, search: searchReducer,
});

const store = configureStore({
  reducer: root,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(
      thunk).concat(createStateSyncMiddleware()),
});
initStateWithPrevTab(store);

export default store;


