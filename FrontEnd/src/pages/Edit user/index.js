import React, { Component } from "react";
import * as business from "../../constants/Business";
import Menu from "../../components/Menu";
import Navbar from "../../components/Navbar";
import EditUser from "../../components/Edit User";

const EditUser_Page = () => {
    return (
      <div id="Home">
        <Navbar businessName="Manage User > Edit User"/>
        <Menu business={business.MANAGE_USER}/>
        <EditUser/>
      </div>
    );
}

export default EditUser_Page;
