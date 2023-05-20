import {useDispatch, useSelector} from "react-redux";
import {Pagination} from "react-bootstrap";
import {useNavigate, useLocation} from "react-router-dom";
import {updateSearchParams} from "../../actions/searchParamsAction";

const RenderPagination = ({size, totalPages, number, fetchCertificates}) => {
  const paginationData = useSelector((state) => state.data.page);
  const paginationState = useSelector((state) => state.search);
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const searchParams = new URLSearchParams(location.search);

  const name = searchParams.get("name");
  const tags = searchParams.getAll("tags");

  if (paginationData) {
    size = paginationState.size || paginationData.size;
    number = paginationData.number + 1 || parseInt(searchParams.get("page"));
    totalPages = paginationData.totalPages || 1;
  }

  const handlePageClick = (pageNum) => {
    const queryParams = new URLSearchParams();
    if (tags) {
      tags.forEach((tag) => {
        queryParams.append("tags", tag);
      });
    }
    if (name) {
      queryParams.set("name", name);
    }
    queryParams.set("page", parseInt(pageNum));
    queryParams.set("size", size);
    navigate(`?${queryParams.toString()}`);

    const searchParams = {
      page: pageNum,
      size: size,
      name: name ? name : null,
      description: null,
      tags: tags ? tags : [],
      date_sort: "asc",
      name_sort: null,
    }
    dispatch(updateSearchParams(searchParams));
  };

  const getPageRange = () => {
    const range = [];
    const visiblePages = 10; // Number of visible page numbers to display

    let start = Math.max(0, number - Math.floor(visiblePages / 2));
    let end = Math.min(totalPages - 1, start + visiblePages - 1);
    start = Math.max(0, end - visiblePages + 1);

    for (let i = start + 1; i < end; i++) {
      range.push(i);
    }

    return range;
  };
  const pageRange = getPageRange();

  return (<>
        <Pagination>
          <Pagination.First
              disabled={number === 1}
              onClick={() => handlePageClick(1)}
          />
          <Pagination.Prev
              disabled={number === 1}
              onClick={() => handlePageClick(number - 1)}
          />
          <Pagination.Item
              active={number === 1}
              onClick={() => handlePageClick(1)}
          >
            {1}
          </Pagination.Item>
          {pageRange.map((pageNum) => (<Pagination.Item
                  active={number - 1 === pageNum}
                  key={pageNum}
                  onClick={() => handlePageClick(pageNum + 1)}
              >
                {pageNum + 1}
              </Pagination.Item>))}
          <Pagination.Item
              active={number === totalPages}
              onClick={() => handlePageClick(totalPages)}
          >
            {totalPages}
          </Pagination.Item>
          <Pagination.Next
              disabled={number === totalPages}
              onClick={() => handlePageClick(number + 1)}
          />
          <Pagination.Last
              disabled={number === totalPages}
              onClick={() => handlePageClick(totalPages)}
          />
        </Pagination>
      </>);
};

export default RenderPagination;
