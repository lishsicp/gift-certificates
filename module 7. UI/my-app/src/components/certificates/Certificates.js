import React, {useEffect, useMemo, useCallback, useState} from "react";
import {useNavigate, useLocation} from 'react-router-dom'
import {connect, useSelector, useDispatch} from "react-redux";
import {fetchCertificates} from "../../actions/fetchCertificatesAction";
import {
  resetSearchParams, updateSearchParams, updateSizeParam
} from "../../actions/searchParamsAction"
import {
  LongTextPopup, formatDate, dateDiffInDays, DismissibleError,
} from "../../actions/utils";
import RenderPagination from "./Pagination";
import {
  Container, Table, Row, Col, Button, Badge, Form, InputGroup
} from "react-bootstrap";

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
          <Col md={12} lg={12} xs={10} className="d-flex flex-column">
            <ConnectedRenderCertificates/>
          </Col>
        </Row>
      </Container>
    </main>
  );
};

const RenderErrors = ({errors}) => {
  if (Array.isArray(errors) && errors.length > 0) {
    return (<div className="error-container">
      {errors.map((error, i) => (
          <DismissibleError key={i} errorText={error.errorMessage}/>))}
    </div>);
  } else {
    return (<div className="error-container">
      <DismissibleError key={1} errorText={errors.errorMessage}/>
    </div>);
  }
};

const SearchInput = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const size = useSelector((state) => state.search.size) || searchParams.get(
      "size") || 10;
  const page = searchParams.get("page") || 1;

  const handleSearch = useCallback((event) => {
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

    const searchParams = {
      page: page,
      size: size,
      name: cleanedSearchText ? cleanedSearchText : null,
      description: null,
      tags: searchTags ? searchTags.map((tag) => tag.substring(1)) : null,
      date_sort: "asc",
      name_sort: null,
    };

    dispatch(updateSearchParams(searchParams));

    const queryString = queryParams.toString();
    navigate(`?${queryString}`);
  }, [dispatch, navigate, page, size]);

  const resetSearchState = useCallback(() => {
    dispatch(resetSearchParams());
    navigate(``)
  }, [dispatch, navigate]);

  return (<InputGroup>
    <Form onSubmit={handleSearch} className="d-flex w-100">
      <Form.Control
          id="search"
          name="search"
          placeholder="Search for certificates"
          aria-label="Search for certificates"
          aria-describedby="search-for-certificates"
      />
      <Button type="submit" variant="outline-primary"
              onClick={() => resetSearchState()}>
        Go
      </Button>
    </Form>
  </InputGroup>);
};

const RenderCertificatesTable = ({fetchCertificates}) => {
  const paginationState = useSelector((state) => state.search);
  const location = useLocation();

  const searchParams = new URLSearchParams(location.search);
  const size = paginationState.size || searchParams.get('size') || 10;
  const page = searchParams.get('page') || 1;
  const name = searchParams.get('name') || '';
  const tags = searchParams.getAll('tags') || [];

  const tagsJson = JSON.stringify(tags);

  const fetch = useCallback(() => {
    fetchCertificates(page, size, {name: name, tags: tags});
  }, [fetchCertificates, page, size, name, tagsJson]);

  const fetchMemo = useMemo(() => fetch, [fetch]);

  useEffect(() => {
    fetchMemo()
  }, [fetchMemo]);

  const certificates = useSelector(
      (state) => mapStateToProps(state).data.certificates);
  const errors = useSelector((state) => mapStateToProps(state).data.errors);

  return (<>
    <SearchInput/>
    {errors.length > 0 ? <RenderErrors errors={errors}/> : ''}
    <Table striped hover>
      <thead>
      <tr>
        <th className="text-center">Name</th>
        <th className="text-center">Description</th>
        <th className="text-center">Creation Date</th>
        <th className="text-center">Price</th>
        <th className="text-center">Valid for</th>
        <th className="text-center">Tags</th>
        <th className="text-center">Actions</th>
      </tr>
      </thead>
      <tbody>
      <RenderCertificatesTableBody certificates={certificates}/>
      </tbody>
    </Table>
    <div
        className="d-flex flex-row justify-content-evenly align-items-center">
      <RenderPagination/>
      <PageSizeSelector/>
    </div>
  </>);
};

const PageSizeSelector = () => {
  const dispatch = useDispatch();
  const [selectedSize, setSelectedSize] = useState(10);
  const currentSize = useSelector((state) => state.search.size) || 10;

  useEffect(() => {
    if (selectedSize !== currentSize) {
      dispatch(updateSizeParam(selectedSize));
    }
  }, [selectedSize, currentSize, dispatch]);

  const handleSizeChange = (event) => {
    const newSize = parseInt(event.target.value);
    setSelectedSize(newSize);
  };

  return (<Form.Select
      id="size"
      size="md"
      style={{width: "5%"}}
      onChange={handleSizeChange}
  >
    <option selected={selectedSize === 5} value={5}>5</option>
    <option selected={selectedSize === 10} value={10}>10</option>
    <option selected={selectedSize === 20} value={20}>20</option>
    <option selected={selectedSize === 50} value={50}>50</option>
  </Form.Select>);
};

const RenderCertificatesTableBody = ({certificates}) => {
  if (!certificates) {
    return null;
  }

  if (!certificates._embedded) {
    return (<tr>
      <td colSpan="7" className="text-center">
        Certificates with these parameters do not exist
      </td>
    </tr>);
  }

  return certificates._embedded.giftCertificateDtoList.map(
      (certificate, i) => (<tr key={i}>
        <td className="align-middle">
          <LongTextPopup maxLength={25} text={certificate.name}/>
        </td>
        <td className="align-middle">
          <LongTextPopup text={certificate.description}/>
        </td>
        <td className="align-middle">
          {formatDate(certificate.createDate)}
        </td>
        <td className="align-middle">{certificate.price}</td>
        <td className="align-middle">
          {dateDiffInDays(certificate.createDate, certificate.duration)
              + " days"}
        </td>
        <td className="align-middle w-25 text-center">
          <div className="d-flex flex-wrap">
            {certificate.tags.map((tag, tagIndex) => (
                <Badge pill bg="dark" className="m-1 p-2 text-truncate"
                       key={tagIndex}>
                  <LongTextPopup maxLength={15}
                                 text={tag.name.replace(/\s+\d+$/, "")}/>
                </Badge>))}
          </div>
        </td>

        <td className="align-middle">
          <div className="row no-gutters justify-content-start">
            <div className="col-auto px-0">
              <Button variant="danger" size="sm" className="mx-0">
                Delete
              </Button>
            </div>
            <div className="col-auto px-0">
              <Button variant="warning" size="sm" className="mx-0">
                Edit
              </Button>
            </div>
            <div className="col-auto px-0">
              <Button variant="info" size="sm" className="mx-0">
                View
              </Button>
            </div>
          </div>
        </td>

      </tr>));
};

const mapStateToProps = (state) => {
  return {
    data: state.data, search: state.search
  };
};

const ConnectedRenderCertificates = connect(mapStateToProps,
    {fetchCertificates, updateSearchParams, resetSearchParams})(
    RenderCertificatesTable);

export default Certificates;
