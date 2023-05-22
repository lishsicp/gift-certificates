import {
  fetchCertificatesRequest,
  fetchCertificatesSuccess,
  fetchCertificatesFailure,
} from "../features/certificatesSlice";
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
          API_ROOT + "/certificates?" + params.toString()
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
