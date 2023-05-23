import {
  fetchCertificatesRequest,
  fetchCertificatesSuccess,
  fetchCertificatesFailure,
} from "../features/certificatesSlice";
import {
  addCertificateRequest,
  addCertificateSuccess,
  addCertificateFailure,
  resetCertificateState
} from "../features/addCertificateSlice";
import {
  deleteCertificateRequest,
  deleteCertificateSuccess,
  deleteCertificateFailure,
  resetDeletionState,
} from "../features/deleteCertificateSlice";
import {API_ROOT} from "../constants";

export const fetchCertificates = (page, size, searchParams = {}) => {
  return async (dispatch) => {
    try {
      dispatch(fetchCertificatesRequest());

      const params = new URLSearchParams();
      params.set("size", size);
      params.set("page", page);

      if (searchParams.name) {
        params.set("name", searchParams.name);
      }
      if (searchParams.description) {
        params.set("description", searchParams.description);
      }
      if (searchParams.tags && searchParams.tags.length > 0) {
        const tags = JSON.parse(searchParams.tags);
        tags.forEach((tag) => {
          params.append("tags", tag);
        });
      }
      if (searchParams.name_sort) {
        params.set("name_sort", searchParams.name_sort);
      }
      if (searchParams.date_sort) {
        params.set("date_sort", searchParams.date_sort);
      }

      const response = await fetch(
        `${API_ROOT}/certificates?${params.toString()}`
      );

      if (response.status !== 200) {
        const errors = await response.json();
        dispatch(fetchCertificatesFailure(errors));
      } else if (response.status === 200) {
        const certificates = await response.json();
        dispatch(fetchCertificatesSuccess(certificates));
      }
    } catch (error) {
      console.log(error);
      dispatch(
          fetchCertificatesFailure([
            {errorMessage: "There's an error. Please try again later."},
          ])
      );
    }
  };
};

export const addCertificate = (certificate, isEdit = false) => {
  return async (dispatch) => {
    try {
      dispatch(addCertificateRequest());

      const response = await fetch(
          API_ROOT + "/certificates", {
            method: isEdit ? "patch" : "post",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(certificate),
          }
      );

      if (response.status !== 201) {
        const errors = await response.json();
        dispatch(addCertificateFailure(errors));
      } else if (response.status === 201) {
        const certificate = await response.json();
        dispatch(addCertificateSuccess(certificate));
      }
    } catch (error) {
      console.log(error);
      dispatch(
          addCertificateFailure([
            {errorMessage: "There's an error. Please try again later."},
          ])
      );
    }
  }
}

export const deleteCertificate = (id) => {
  return async (dispatch) => {
    try {
      dispatch(deleteCertificateRequest());

      const response = await fetch(
        `${API_ROOT}/certificates/${id}`, {
            method: "delete",
            headers: {
              "Content-Type": "application/json",
            }
          }
      );

      if (response.status !== 204) {
        const errors = await response.json();
        dispatch(deleteCertificateFailure(errors));
      } else if (response.status === 204) {
        dispatch(deleteCertificateSuccess());
      }
    } catch (error) {
      console.log(error);
      dispatch(
        deleteCertificateFailure([
            {errorMessage: "There's an error. Please try again later."},
          ])
      );
    }
  }
}