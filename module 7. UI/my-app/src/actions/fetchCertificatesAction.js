import {API_ROOT} from "../constants";

const FETCH_CERTIFICATES_SUCCESS = "FETCH_CERTIFICATES_SUCCESS";
const FETCH_CERTIFICATES_FAILURE = "FETCH_CERTIFICATES_FAILURE";
const FETCH_CERTIFICATES_REQUEST = "FETCH_CERTIFICATES_REQUEST";

const fetchCertificatesRequest = () => {
  return {
    type: FETCH_CERTIFICATES_REQUEST,
  };
};

const fetchCertificatesSuccess = (certificates) => {
  return {
    type: FETCH_CERTIFICATES_SUCCESS, payload: certificates,
  };
};

const fetchCertificatesFailure = (errors) => {
  return {
    type: FETCH_CERTIFICATES_FAILURE, payload: errors,
  };
};

export const fetchCertificates = (page, size, searchParams = {}) => {
  return async (dispatch) => {
    try {
      dispatch(fetchCertificatesRequest());

      const params = new URLSearchParams();
      params.set('size', size);
      params.set('page', page);
      params.set('name_sort', 'asc');
      params.set('date_sort', 'asc');

      // Set additional search parameters
      if (searchParams.name) {
        params.set('name', searchParams.name);
      }
      if (searchParams.description) {
        params.set('description', searchParams.description);
      }
      if (searchParams.tags && searchParams.tags.length > 0) {
        searchParams.tags.forEach((tag) => {
          params.append('tags', tag);
        });
      }

      const response = await fetch(
          API_ROOT + "/certificates?" + params.toString());

      if (response.status !== 200) {
        const errors = await response.json();
        dispatch(fetchCertificatesFailure(errors));
      } else if (response.status === 200) {
        const certificates = await response.json();
        dispatch(fetchCertificatesSuccess(certificates));
      }
    } catch (error) {
      console.log(error)
      dispatch(fetchCertificatesFailure(
          [{errorMessage: "There's an error. Please try again later."},]));
    }
  };
};

