import { addCertificateRequest } from "../features/certificate/addCertificateSlice";
import { deleteCertificateRequest } from "../features/certificate/deleteCertificateSlice";
import { protecredApiSlice } from "./protecredApiSlice";

export const certificatesApiMutations = protecredApiSlice.injectEndpoints({
  endpoints: (builder) => ({
    addCertificate: builder.mutation({
      query: (data) => ({
        url: "/certificates",
        method: "POST",
        body: data,
      }),
      onQueryStarted: (_, { dispatch }) => {
        dispatch(addCertificateRequest());
      },
    }),
    editCertificate: builder.mutation({
      query: ({ id, ...data }) => ({
        url: `/certificates/${id}`,
        method: "PATCH",
        body: data,
      }),
      onQueryStarted: (_, { dispatch }) => {
        dispatch(addCertificateRequest());
      },
    }),
    deleteCertificate: builder.mutation({
      query: (id) => ({
        url: `/certificates/${id}`,
        method: "DELETE",
      }),
      onQueryStarted: (_, { dispatch }) => {
        dispatch(deleteCertificateRequest());
      },
    }),
  }),
});

export const {
  useAddCertificateMutation,
  useEditCertificateMutation,
  useDeleteCertificateMutation,
} = certificatesApiMutations;
