import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import Reports from "../../components/Asset Report";
import Navbar from "../../components/Navbar";

const Report_Page = () => {
  return (
    <div id="Home">
      <Navbar businessName="Report" />
      <Menu business={business.REPORT} />
      {/* menu co prop là business tương ứng HOME, MANAGE_USER, ... mn xem them trong const */}
      {/* layout mẫu mn cu v mà làm nha */}
      <Reports />
    </div>
  );
};

export default Report_Page;
