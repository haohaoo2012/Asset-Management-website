import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import EditAssignment from "../../components/EditAssignment";
import Navbar from "../../components/Navbar";

const index = () => {
  return (
    <div id="Home">
      <Navbar businessName="Manage Assignmnet > Edit Assignment" />
      <Menu business={business.MANAGE_ASSIGNMENT} />
      {/* menu co prop là business tương ứng HOME, MANAGE_USER, ... mn xem them trong const */}
      {/* layout mẫu mn cu v mà làm nha */}
      <EditAssignment />
    </div>
  );
};

export default index;
