import React, { useEffect, useState } from "react";
import { useParams } from "react-router";
import { del } from "../../httpHelper";
import './DeleteAsset.css'

export const DeleteAsset_Yes = (props) => {
    const handleConfirmDeleteAsset = () => {
        let Url=`asset/${props.assetCode}`;
        del(Url)
            .then(() => {
                alert("Delete asset OK!");
                props.handleDeleteAssetShow();
            })
            .catch(response => console.log(response))
    }

    const handleCancelDeleteAsset = () => {
        props.handleDeleteAssetShow();
    }

    return (
        <div id="delete-asset-yes" className={!props.deleteAssetYES ? "show-delete-asset" : ""}>
            <div className="delete-asset-header">
                <p>Are you sure?</p>
            </div>
            <div className="delete-asset-body">
                <p>Do you want to delete this asset?</p>
                <div className="button-flex">
                    <button className="delete-btn" onClick={handleConfirmDeleteAsset}>DELETE</button>
                    <button className="cancel-btn" onClick={handleCancelDeleteAsset}>CANCEL</button>
                </div>
            </div>
        </div>
    )
}

export const DeleteAsset_No = (props) => {
    const handleXButton = () => {
        props.handleDeleteAssetShow();
    }

    return (
        <div id="delete-asset-no" className={!props.deleteAssetNO ? "show-delete-asset" : ""}>
            <div className="delete-asset-header">
                <p>Cannot Delete Asset</p>
                <button className="x-btn" onClick={handleXButton}>X</button>
            </div>
            <div className="delete-asset-body">
                <p>Cannot delete the asset because it belongs to one or more historical assignments.</p>
                <div className="link-edit-asset">
                    <p>If the asset is not able to be used anymore, please update its state in &nbsp;
                        <a href={`/editasset/${props.assetCode}`}>Edit Asset page</a>
                    </p>
                </div>
            </div>
        </div>
    )
}