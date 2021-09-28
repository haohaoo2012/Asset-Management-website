import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import Users from "../../components/Users";
import Navbar from "../../components/Navbar";

const ManageUser_Page = () => {
  return (
    <div id="Home">
      <Navbar businessName="Manage User" />
      <Menu business={business.MANAGE_USER} />
      {/* menu co prop là business tương ứng HOME, MANAGE_USER, ... mn xem them trong const */}
      {/* layout mẫu mn cu v mà làm nha */}
      <Users />
    </div>
  );
};

export default ManageUser_Page;
