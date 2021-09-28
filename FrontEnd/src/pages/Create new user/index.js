import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import CreateNewUser from "../../components/Create New User";
import Navbar from "../../components/Navbar";

const CreateNewUser_Page = () => {
    return (
      <div id="Home">
        <Navbar businessName="Manage User > Create New User"/>
        <Menu business={business.MANAGE_USER}/>
        {/* menu co prop là business tương ứng HOME, MANAGE_USER, ... mn xem them trong const */}
        {/* layout mẫu mn cu v mà làm nha */}
        <CreateNewUser/>
      </div>
    );
}

export default CreateNewUser_Page;
