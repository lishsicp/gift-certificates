import {useSelector} from "react-redux";
import {Pagination} from "react-bootstrap";
import {useNavigate, useLocation} from "react-router-dom";

const RenderPagination = () => {
  const paginationData = useSelector((state) => state.data.page);
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  let number = parseInt(searchParams.get('page')) || 1;
  const size = parseInt(searchParams.get('size')) || 10;

  let totalPages;
  if (paginationData) {
    totalPages = paginationData.totalPages;
    if (number > totalPages) {
      number = totalPages;
    }
  }

  const handlePageClick = (pageNum) => {
    searchParams.set("page", parseInt(pageNum));
    searchParams.set("size", size);
    navigate(`?${searchParams.toString()}`);
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
