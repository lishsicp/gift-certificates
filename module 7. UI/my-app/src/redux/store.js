import {
  configureStore, combineReducers,
} from "@reduxjs/toolkit";
import thunk from "redux-thunk";
import searchReducer from '../features/searchSlice';
import certificatesReducer from '../features/certificatesSlice';
import addCertificatesReducer from '../features/addCertificateSlice';
import deleteCertificateReducer from '../features/deleteCertificateSlice';

const root = combineReducers({
  data: certificatesReducer,
  search: searchReducer, 
  addCertificate: addCertificatesReducer,
  deleteCertificate: deleteCertificateReducer,

});

const store = configureStore({
  reducer: root,
  middleware: getDefaultMiddleware => getDefaultMiddleware().concat(thunk),
  devTools: true
});


export default store;


