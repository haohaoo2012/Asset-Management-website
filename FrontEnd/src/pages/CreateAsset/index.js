import React, { Component } from "react";
import Navbar from "../../components/Navbar";
import Menu from "../../components/Menu";
import * as business from "../../constants/Business";
import CreateAsset from "../../components/CreateAsset";

export default class index extends Component {
  render() {
    return (
      <div id="Home">
        <Navbar businessName="Manage Asset > Create New Asset"/>
        <Menu business={business.MANAGE_ASSET}/>
        {/* menu co prop là business tương ứng HOME, MANAGE_USER, ... mn xem them trong const */}
        {/* layout mẫu mn cu v mà làm nha */}
        <CreateAsset/>
      </div>
    );
  }
}