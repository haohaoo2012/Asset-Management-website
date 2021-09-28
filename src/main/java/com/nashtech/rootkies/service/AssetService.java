package com.nashtech.rootkies.service;

import com.nashtech.rootkies.dto.PageDTO;
import com.nashtech.rootkies.dto.asset.request.EditAssetRequest;
import com.nashtech.rootkies.dto.asset.request.SearchFilterSortAssetDTO;
import com.nashtech.rootkies.dto.asset.response.EditAssetDTO;
import com.nashtech.rootkies.dto.asset.response.ReportDTO;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.exception.AssetConvertException;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.DeleteDataFailException;
import com.nashtech.rootkies.model.Asset;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface AssetService {
        // Asset list can be sorted by column titles (default: ascending)

        // -------------------------------------------------------------------------------------
        // By default, it displays all assets having state = Available, Not available,
        // Assigned and location of the user
        public ResponseDTO countAsset(Long locationId) throws DataNotFoundException;

        public ResponseDTO retrieveAsset(Pageable page, Long locationId) throws DataNotFoundException;
        // -------------------------------------------------------------------------------------

        public ResponseDTO retrieveAssetByAssetCode(Long locationId, String assetCode) throws DataNotFoundException;

        public ResponseDTO retrieveAssetHavingFilterSearchSort(Integer pageNum, Integer numOfItems,
                        SearchFilterSortAssetDTO searchFilterSortAssetDTO, Long locationId)
                        throws DataNotFoundException;

        public ResponseDTO countAssetHavingFilterSearchSort(SearchFilterSortAssetDTO searchFilterSortAssetDTO,
                        Long locationId) throws DataNotFoundException;

        public ResponseDTO saveAsset(Asset asset) throws CreateDataFailException;

        public EditAssetDTO editAsset(String assetCode, EditAssetRequest editAssetRequest);

        public ResponseDTO deleteAssetByAssetCode(Long locationId, String assetCode)
                        throws DataNotFoundException, DeleteDataFailException;

        public ResponseDTO checkDeleteAssetByAssetCode(Long locationId, String assetCode)
                        throws DataNotFoundException, DeleteDataFailException;

        PageDTO getAllAssetAvailable(Pageable pageable , Specification specification) throws DataNotFoundException;

        List<ReportDTO> getAssetReport(Long locationId) throws AssetConvertException;
}
