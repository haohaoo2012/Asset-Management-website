import React from "react";
import Pagination from "@material-ui/lab/Pagination";
import "./pagination.css";

const Paginations = ({ totalPages, setCurrentPage }) => {
  return (
    <div className="pagination-edit-assignment">
      <Pagination
        count={totalPages}
        variant="outlined"
        color="primary"
        onChange={(event, value) => setCurrentPage(value - 1)}
      />
    </div>
  );
};

export default Paginations;
