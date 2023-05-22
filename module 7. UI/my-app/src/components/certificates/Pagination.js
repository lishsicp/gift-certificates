import {useSelector} from "react-redux";
import {Pagination} from "react-bootstrap";
import {useNavigate, useLocation} from "react-router-dom";

const RenderPagination = () => {
  const paginationData = useSelector((state) => state.data.page);
  //const paginationState = useSelector((state) => state.search);
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  let totalPages = 1;

  let number = parseInt(searchParams.get('page')) || 1;
  const size = parseInt(searchParams.get('size')) || 10;
  const name = searchParams.get("name");
  const tags = searchParams.getAll("tags");
  const date_sort = searchParams.get("date_sort");
  const name_sort = searchParams.get("name_sort");

  if (paginationData) {
    // size = paginationState.size || paginationData.size;
    // number = paginationData.number + 1 || parseInt(searchParams.get("page"));
    totalPages = paginationData.totalPages;
    if (number > totalPages) {
      number = totalPages || 1;
    }
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
    if (name_sort) {
      queryParams.set('name_sort', name_sort);
    }
    if (date_sort) {
      queryParams.set('date_sort', date_sort);
    }
    queryParams.set("page", parseInt(pageNum));
    queryParams.set("size", size);
    navigate(`?${queryParams.toString()}`);
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
          hidden={totalPages === 1}
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
