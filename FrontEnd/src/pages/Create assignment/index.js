import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import CreateAssignment from "../../components/Create Assignment";
import Navbar from "../../components/Navbar";

const Create_Assignment_Page = () => {
  return (
    <div id="Home">
      <Navbar businessName="Manage Assignmnet > Create Assignment" />
      <Menu business={business.MANAGE_ASSIGNMENT} />
      {/* menu co prop là business tương ứng HOME, MANAGE_USER, ... mn xem them trong const */}
      {/* layout mẫu mn cu v mà làm nha */}
      <CreateAssignment />
    </div>
  );
};

export default Create_Assignment_Page;
