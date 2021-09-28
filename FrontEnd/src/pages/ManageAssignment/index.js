import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import Assignment from "../../components/ManageAssignment/Assignment";
import Navbar from "../../components/Navbar";

const ManageAssignment_Page = () => {
  return (
    <div id="MANAGE_ASSIGNMENT">
      <Navbar businessName="Manage Assignment" />
      <Menu business={business.MANAGE_ASSIGNMENT} />
      <Assignment/>
    </div>
  );
};

export default ManageAssignment_Page;
