import * as certificatesActions from "../features/certificate/certificatesSlice";
import { API_ROOT } from "../constants";

export const fetchCertificates = (searchParams = {}) => {
  return async (dispatch) => {
    try {
      dispatch(certificatesActions.fetchCertificatesRequest());

      const params = new URLSearchParams({
        ...(searchParams.size ? { size: searchParams.size } : { size: 10 }),
        ...(searchParams.page ? { page: searchParams.page } : { page: 1 }),
        ...(searchParams.name && { name: searchParams.name }),
        ...(searchParams.description && {
          description: searchParams.description,
        }),
        ...(searchParams.name_sort && { name_sort: searchParams.name_sort }),
        ...(searchParams.date_sort
          ? { date_sort: searchParams.date_sort }
          : { date_sort: "desc" }),
      });

      if (searchParams.tags && searchParams.tags.length > 0) {
        const tags = JSON.parse(searchParams.tags);
        tags.forEach((tag) => {
          params.append("tags", tag);
        });
      }

      const response = await fetch(
        `${API_ROOT}/certificates?${params.toString()}`
      );
      if (!response.ok) {
        const errors = await response.json();
        dispatch(certificatesActions.fetchCertificatesFailure(errors));
      } else {
        const certificates = await response.json();
        dispatch(certificatesActions.fetchCertificatesSuccess(certificates));
      }
    } catch (error) {
      console.log(error);
      dispatch(
        certificatesActions.fetchCertificatesFailure([
          { errorMessage: "There's an error. Please try again later." },
        ])
      );
    }
  };
};
