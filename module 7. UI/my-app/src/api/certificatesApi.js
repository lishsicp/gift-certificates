import * as certificatesActions from "../features/certificatesSlice";
import * as addCertificateActions from "../features/addCertificateSlice";
import * as deleteCertificateActions from "../features/deleteCertificateSlice";
import {API_ROOT} from "../constants";

const createAction = (method) => {
  return (data) => async (dispatch) => {
    try {
      dispatch(addCertificateActions.addCertificateRequest());
      let requestUrl = `${API_ROOT}/certificates/`;
      if (method === 'patch') requestUrl += data.id;
      const response = await fetch(requestUrl, {
        method: method, headers: {
          "Content-Type": "application/json"
        }, body: JSON.stringify(data),
        mode: "cors"
      });
      if (!response.ok) {
        const errors = await response.json();
        dispatch(addCertificateActions.addCertificateFailure(errors));
      } else {
        const result = await response.json();
        dispatch(addCertificateActions.addCertificateSuccess(result));
      }
    } catch (error) {
      console.log(error);
      dispatch(addCertificateActions.addCertificateFailure(
          [{errorMessage: "There's an error. Please try again later."},]));
    }
  };
};

export const editCertificate = createAction("patch");
export const addCertificate = createAction("post");

export const fetchCertificates = (page, size, searchParams = {}) => {
  return async (dispatch) => {
    try {
      dispatch(certificatesActions.fetchCertificatesRequest());

      const params = new URLSearchParams({
        size: size,
        page: page,
        ...(searchParams.name) && {name: searchParams.name},
        ...(searchParams.description) && {description: searchParams.description},
        ...(searchParams.name_sort) && {name_sort: searchParams.name_sort},
        ...(searchParams.date_sort) && {date_sort: searchParams.date_sort},
      });

      if (searchParams.tags && searchParams.tags.length > 0) {
        const tags = JSON.parse(searchParams.tags);
        tags.forEach((tag) => {
          params.append("tags", tag);
        });
      }

      const response = await fetch(
          `${API_ROOT}/certificates?${params.toString()}`);
      if (!response.ok) {
        const errors = await response.json();
        dispatch(certificatesActions.fetchCertificatesFailure(errors));
      } else {
        const certificates = await response.json();
        dispatch(certificatesActions.fetchCertificatesSuccess(certificates));
      }
    } catch (error) {
      console.log(error);
      dispatch(certificatesActions.fetchCertificatesFailure(
          [{errorMessage: "There's an error. Please try again later."},]));
    }
  };
};

export const deleteCertificate = (id) => async (dispatch) => {
  try {
    dispatch(deleteCertificateActions.deleteCertificateRequest());

    const response = await fetch(`${API_ROOT}/certificates/${id}`, {
      method: "delete", headers: {"Content-Type": "application/json",},
    });
    if (!response.ok) {
      const errors = await response.json();
      dispatch(deleteCertificateActions.deleteCertificateFailure(errors));
    } else {
      dispatch(deleteCertificateActions.deleteCertificateSuccess());
    }
  } catch (error) {
    console.log(error);
    dispatch(deleteCertificateActions.deleteCertificateFailure(
        [{errorMessage: "There's an error. Please try again later."},]));
  }
}
