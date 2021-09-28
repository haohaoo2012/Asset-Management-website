import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import ManageRequest from "../../components/ManageRequest";
import Navbar from "../../components/Navbar";

const index = () => {
  return (
    <div id="Home">
      <Navbar businessName="Manage Request" />
      <Menu business={business.REQUEST_FOR_RETURNING} />
      <ManageRequest />
    </div>
  );
};

export default index;
