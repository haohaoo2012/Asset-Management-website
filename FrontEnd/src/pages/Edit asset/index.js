import React from "react";
import Navbar from "../../components/Navbar";
import Menu from "../../components/Menu";
import * as business from "../../constants/Business";
import EditAsset from "../../components/Manage Asset_Edit/Edit";

const EditAsset_Page = () => {
    return (
      <div id="EditAsset">
        <Navbar businessName=" Manage Asset > Edit Asset" />
        <Menu business={business.MANAGE_ASSET} />
        <EditAsset/>
        
      </div>
    );
}

export default EditAsset_Page;
