const API_ROOT = "http://localhost:8080/api";

export async function fetchCertificates(page, size) {
    try {
      const response = await fetch(API_ROOT + '/certificates?size=' + size + "&page=" + page);
      const contentType = response.headers.get("content-type");
      if (!contentType || !contentType.includes("application/json")) {
        return { errorMessage: "Oops, we haven't got JSON!"};
      }
      if (response.status !== 200) {
        return await response.json();
      }
      const certificates = await response.json();
      return certificates._embedded.giftCertificateDtoList;
    } catch (error) {
      console.error("Error:", error);
    }
}