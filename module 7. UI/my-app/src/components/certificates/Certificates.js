import React, { useEffect, useCallback, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { connect, useSelector, useDispatch } from "react-redux";
import { fetchCertificates } from "../../api/certificatesApiQueries";
import {
  useAddCertificateMutation,
  useDeleteCertificateMutation,
  useEditCertificateMutation,
} from "../../api/certificatesApiMutations";
import {
  resetSearch,
  updateDateSort,
  updateNameSort,
  updateSize,
} from "../../features/certificate/searchSlice";
import {
  LongTextPopup,
  formatDate,
  dateDiffInDays,
  DismissibleError,
  DismissibleSuccess,
} from "../utils";
import RenderPagination from "./Pagination";
import {
  Container,
  Table,
  Row,
  Col,
  Button,
  Badge,
  Form,
  InputGroup,
  Spinner,
} from "react-bootstrap";
import { ArrowUp, ArrowDown } from "react-bootstrap-icons";
import DeleteModal from "./modals/deleteModal";
import ViewModal from "./modals/viewModal";
import AddCertificateModal from "./modals/addCertificateModal";
import { resetDeletionState } from "../../features/certificate/deleteCertificateSlice";
import { resetCertificateState } from "../../features/certificate/addCertificateSlice";
import { resetCertificatesState } from "../../features/certificate/certificatesSlice";
import { useTokenMutation } from "../../api/tokenApiSlice";
import { updateAccessToken } from "../../features/auth/authTokenSlice";
import RequireAuth from "../../features/auth/RequireAuth";

const Certificates = () => {
  return (
    <main className="w-100">
      <Container
        className="d-flex justify-content-center align-items-center"
        fluid="lg"
      >
        <Row>
          <h2 className="fw-bold mt-2 text-uppercase bg-dark p-3 justify-content-center text-light w-100">
            Gift Certificates
          </h2>
          <Authorize />
          <Col md={12} lg={12} xs={10} className="d-flex flex-column">
            <ConnectedRenderCertificates />
          </Col>
        </Row>
      </Container>
    </main>
  );
};

const Authorize = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const searchParams = new URLSearchParams(location.search);
  const [code, setCode] = useState(searchParams.get("code"));
  const [tokenMutation, { isLoading, isError, isSuccess }] = useTokenMutation();

  const [error, setError] = useState("");

  const emptyFunction = () => {
    // No statements
  };

  const tokenCallback = useCallback(() => {
    if (code) {
      tokenMutation(code)
        .then((response) => {
          console.debug(response);
          const tokenData = response.data;
          setCode(null);
          dispatch(updateAccessToken(tokenData));
        })
        .catch((error) => {
          console.error(error);
          if (!error?.status) {
            setError("No Server Response");
          } else if (error.status === 400) {
            setError("Something wrong with request.");
          } else if (error.status === 401) {
            setError("Unauthorized");
          } else {
            setError("Login Failed");
          }
        })
        .finally(navigate(""));
    }
  }, [code, tokenMutation, dispatch, navigate]);

  useEffect(() => {
    tokenCallback();
  }, [tokenCallback]);

  return (
    <>
      {isLoading ? (
        <Spinner
          size="lg"
          animation="border"
          variant="dark"
          className="d-flex justify-content-center align-self-center my-5"
        />
      ) : null}
      {isError ? (
        <RenderErrors errors={error} resetFunction={() => emptyFunction()} />
      ) : null}
      {isSuccess ? (
        <DismissibleSuccess message="Successfully logged in!" />
      ) : null}
    </>
  );
};

const RenderErrors = ({ errors, resetFunction }) => {
  const dispatch = useDispatch();

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      dispatch(resetFunction());
    }, 5000);
    return () => clearTimeout(timeoutId);
  }, [dispatch, resetFunction]);

  if (!errors) {
    return null;
  }

  if (!Array.isArray(errors)) {
    errors = [errors];
  }

  if (errors.length > 0) {
    return (
      <div className="error-container">
        {errors.map((error, i) => (
          <DismissibleError key={i} errorText={error.errorMessage} />
        ))}
      </div>
    );
  }

  return null;
};

const SearchInput = ({ search }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const size = searchParams.get("size") || search.size;
  const page = searchParams.get("page") || 1;

  const handleSearch = useCallback(
    (event) => {
      event.preventDefault();
      const formData = new FormData(event.target);
      const searchText = formData.get("search");
      const searchTags = searchText.match(/#(\w+)/g);
      const cleanedSearchText = searchText.replace(/#\w+/g, "").trim();
      const queryParams = new URLSearchParams();
      if (cleanedSearchText) {
        queryParams.set("name", cleanedSearchText);
        queryParams.set("description", cleanedSearchText);
      }
      if (searchTags) {
        searchTags.forEach((tag) => {
          queryParams.append("tags", tag.substring(1)); // Remove the leading '#'
        });
      }
      queryParams.set("size", size);
      queryParams.set("page", page);

      const queryString = queryParams.toString();
      navigate(`?${queryString}`);
    },
    [navigate, page, size]
  );

  const resetSearchState = useCallback(() => {
    dispatch(resetSearch());
    navigate(``);
  }, [dispatch, navigate]);

  return (
    <InputGroup>
      <Form onSubmit={handleSearch} className="d-flex w-100">
        <Form.Control
          id="search"
          name="search"
          placeholder="Search for certificates..."
          aria-label="Search for certificates"
          aria-describedby="search-for-certificates"
        />
        <Button
          type="submit"
          variant="outline-primary"
          onClick={() => resetSearchState()}
        >
          Search
        </Button>
      </Form>
    </InputGroup>
  );
};

const SortByButton = ({ sortType, sortState }) => {
  const paginationState = useSelector((state) => state.search);
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleSort = useCallback(
    (sortType) => {
      const queryParams = new URLSearchParams(location.search);
      let newSortType;

      if (sortType === "name_sort") {
        newSortType = paginationState[sortType] === "asc" ? "desc" : "asc";
        queryParams.set("name_sort", newSortType);
        queryParams.delete("date_sort");
      } else if (sortType === "date_sort") {
        newSortType = paginationState[sortType] === "asc" ? "desc" : "asc";
        queryParams.set("date_sort", newSortType);
        queryParams.delete("name_sort");
      }

      queryParams.set(sortType, newSortType);
      queryParams.set("page", 1);

      if (newSortType !== paginationState[sortType]) {
        if (sortType === "name_sort") {
          dispatch(updateDateSort(null));
          dispatch(updateNameSort(newSortType));
        } else if (sortType === "date_sort") {
          dispatch(updateNameSort(null));
          dispatch(updateDateSort(newSortType));
        }
      }
      navigate(`?${queryParams}`);
    },
    [dispatch, navigate, location.search, paginationState]
  );

  return (
    <div className="d-flex justify-content-center align-items-center">
      <Button
        className="bg-transparent"
        size="sm"
        variant="white"
        onClick={() => handleSort(sortType)}
      >
        {sortState === "asc" ? (
          <ArrowUp className="sort-icon" />
        ) : (
          <ArrowDown className="sort-icon" />
        )}
      </Button>
    </div>
  );
};

const RenderCertificatesTable = ({
  fetchCertificates,
  data,
  search,
  remove,
  add,
}) => {
  const isDeleteSuccess = remove.isSuccess;
  const isAddSuccess = add.isSuccess;
  const location = useLocation();

  const searchParams = new URLSearchParams(location.search);
  const size = searchParams.get("size") || search.size;
  const page = searchParams.get("page") || 1;
  const name = searchParams.get("name") || "";
  const tags = searchParams.getAll("tags") || [];
  const date_sort = searchParams.get("date_sort") || search.date_sort;
  const name_sort = searchParams.get("name_sort");

  const tagsJson = JSON.stringify(tags);

  const fetch = useCallback(() => {
    fetchCertificates({
      page,
      size,
      name,
      tags: tagsJson,
      date_sort,
      name_sort,
    });
    if (isDeleteSuccess || isAddSuccess) {
      /* this is used as a hook to trigger update on successful operations */
    }
  }, [
    fetchCertificates,
    page,
    size,
    name,
    tagsJson,
    date_sort,
    name_sort,
    isDeleteSuccess,
    isAddSuccess,
  ]);

  useEffect(() => {
    fetch();
  }, [fetch]);

  return (
    <>
      <SearchInput search={search} />
      {remove.errors ? (
        <RenderErrors
          errors={remove.errors}
          resetFunction={resetDeletionState}
        />
      ) : null}
      {add.errors ? (
        <RenderErrors
          errors={add.errors}
          resetFunction={resetCertificateState}
        />
      ) : null}
      {data.errors ? (
        <RenderErrors
          errors={data.errors}
          resetFunction={resetCertificatesState}
        />
      ) : null}
      {add.isSuccess ? (
        <DismissibleSuccess message="Certificate updated!" />
      ) : null}
      {remove.isSuccess ? (
        <DismissibleSuccess message="Certificate deleted!" />
      ) : null}
      {data.loading || remove.loading || add.loading ? (
        <Spinner
          size="lg"
          animation="border"
          variant="dark"
          className="align-self-center my-5"
        />
      ) : (
        <Table striped hover>
          <thead>
            <tr>
              <th className="text-center d-flex justify-content-center align-items-center">
                Name
                <SortByButton
                  sortState={search.name_sort}
                  sortType="name_sort"
                />
              </th>
              <th className="text-center">Description</th>
              <th className="text-center d-flex justify-content-center align-items-center">
                Creation Date
                <SortByButton
                  sortState={search.date_sort}
                  sortType="date_sort"
                />
              </th>
              <th className="text-center">Price</th>
              <th className="text-center">Valid for</th>
              <th className="text-center">Tags</th>
              <th className="text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            <RenderCertificatesTableBody certificates={data.certificates} />
          </tbody>
        </Table>
      )}
      <div className="d-flex justify-content-center align-items-center">
        <RenderPagination className="" paginationData={data.page} />
        <PageSizeSelector className="ml-2 mb-2" />
      </div>
    </>
  );
};

const PageSizeSelector = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const selectedSize = searchParams.get("size") || 10;
  useEffect(() => {
    const handleSizeChange = (event) => {
      const searchParams = new URLSearchParams(location.search);
      const newSize = parseInt(event.target.value);
      searchParams.set("size", newSize);
      searchParams.set("page", 1);
      navigate(`?${searchParams}`);
      dispatch(updateSize(newSize));
    };
    const sizeSelectElement = document.getElementById("size");
    sizeSelectElement.addEventListener("change", handleSizeChange);
    return () => {
      sizeSelectElement.removeEventListener("change", handleSizeChange);
    };
  }, [dispatch, navigate, location.search]);

  return (
    <Form.Select
      id="size"
      size="md"
      style={{ width: "8%" }}
      className="mb-3 ms-3"
      defaultValue={String(selectedSize)}
      onChange={() => {}}
    >
      {[5, 10, 20, 50].map((size) => (
        <option key={size} value={size}>
          {size}
        </option>
      ))}
    </Form.Select>
  );
};

const RenderCertificatesTableBody = ({ certificates }) => {
  if (!certificates) {
    return null;
  }

  if (!certificates._embedded) {
    return (
      <tr>
        <td colSpan="7" className="text-center">
          Certificates with these parameters do not exist
        </td>
      </tr>
    );
  }

  return certificates._embedded.giftCertificateDtoList.map((certificate, i) => (
    <tr key={i}>
      <td className="align-middle">
        <LongTextPopup maxLength={25} text={certificate.name} />
      </td>
      <td className="align-middle">
        <LongTextPopup text={certificate.description} />
      </td>
      <td className="align-middle">{formatDate(certificate.createDate)}</td>
      <td className="align-middle">{certificate.price}</td>
      <td className="align-middle">
        {dateDiffInDays(certificate.createDate, certificate.duration)}
      </td>
      <td className="align-middle w-25 text-center">
        <div className="d-flex flex-wrap">
          {certificate.tags.map((tag, tagIndex) => (
            <Badge
              pill
              bg="dark"
              className="m-1 p-2 text-truncate"
              key={tagIndex}
            >
              <LongTextPopup
                maxLength={15}
                text={tag.name.replace(/\s+\d+$/, "")}
              />
            </Badge>
          ))}
        </div>
      </td>

      <td className="align-middle">
        <div className="row no-gutters justify-content-start">
          <div className="col-auto px-0">
            <RequireAuth
              child={
                <DeleteModal id={certificate.id} name={certificate.name} />
              }
            />
          </div>
          <div className="col-auto px-0">
            <RequireAuth
              child={<AddCertificateModal certificateToEdit={certificate} />}
            />
          </div>
          <div className="col-auto px-0">
            <ViewModal certificate={certificate} />
          </div>
        </div>
      </td>
    </tr>
  ));
};

const mapStateToProps = (state) => {
  return {
    data: state.data,
    search: state.search,
    add: state.addCertificate,
    remove: state.deleteCertificate,
  };
};

const ConnectedRenderCertificates = connect(mapStateToProps, {
  fetchCertificates,
  updateSize,
  resetSearch,
  updateDateSort,
  updateNameSort,
  useAddCertificateMutation,
  useEditCertificateMutation,
  useDeleteCertificateMutation,
})(RenderCertificatesTable);

export default Certificates;
