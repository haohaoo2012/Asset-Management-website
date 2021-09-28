import React, { useState, useEffect } from "react";
import "./report.css";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowDropUpIcon from "@material-ui/icons/ArrowDropUp";
import { get } from "../../httpHelper";
import { Report } from "./Report";
import { Button } from "@material-ui/core";
import axios from "axios";
import * as FileSaver from "file-saver";

import * as XLSX from "xlsx";
import Export from "./Export";
const Reports = () => {
  const [reports, setReports] = useState([]);
  const [isCategoryASC, setIsCategoryASC] = useState(true);
  const [isTotalASC, setIsTotalASC] = useState(true);
  const [isAssignedASC, setIsAssignedASC] = useState(true);
  const [isAvailableASC, setIsAvailableASC] = useState(true);
  const [isNotAvailableASC, setIsNotAvailableASC] = useState(true);
  const [isWaitingASC, setIsWaitingASC] = useState(true);
  const [isRecycledASC, setIsRecycledASC] = useState(true);

  const handleSortByCategory = () => {
    function conpareCateASC(a, b) {
      if (a.category < b.category) {
        return -1;
      }
      if (a.category > b.category) {
        return 1;
      }
      return 0;
    }
    function conpareCateDES(a, b) {
      if (a.category < b.category) {
        return 1;
      }
      if (a.category > b.category) {
        return -1;
      }
      return 0;
    }
    if (isCategoryASC) {
      const newReport = reports.sort(conpareCateASC);
      setReports(newReport);
    } else {
      const newReport = reports.sort(conpareCateDES);
      setReports(newReport);
    }

    setIsCategoryASC(!isCategoryASC);
  };
  const handleSortByTotal = () => {
    function conpareTotalASC(a, b) {
      if (a.total < b.total) {
        return -1;
      }
      if (a.total > b.total) {
        return 1;
      }
      return 0;
    }
    function conpareTotalDES(a, b) {
      if (a.total < b.total) {
        return 1;
      }
      if (a.total > b.total) {
        return -1;
      }
      return 0;
    }
    if (isTotalASC) {
      const newReport = reports.sort(conpareTotalASC);
      setReports(newReport);
    } else {
      const newReport = reports.sort(conpareTotalDES);
      setReports(newReport);
    }
    setIsTotalASC(!isTotalASC);
  };
  const handleSortByAssigned = () => {
    function conpareAssignedASC(a, b) {
      if (a.assigned < b.assigned) {
        return -1;
      }
      if (a.assigned > b.assigned) {
        return 1;
      }
      return 0;
    }
    function conpareAssignedDES(a, b) {
      if (a.assigned < b.assigned) {
        return 1;
      }
      if (a.assigned > b.assigned) {
        return -1;
      }
      return 0;
    }
    if (isAssignedASC) {
      const newReport = reports.sort(conpareAssignedASC);
      setReports(newReport);
    } else {
      const newReport = reports.sort(conpareAssignedDES);
      setReports(newReport);
    }
    setIsAssignedASC(!isAssignedASC);
  };
  const handleSortByAvailable = () => {
    function conpareAvailableASC(a, b) {
      if (a.available < b.available) {
        return -1;
      }
      if (a.available > b.available) {
        return 1;
      }
      return 0;
    }
    function conpareAvailableDES(a, b) {
      if (a.available < b.available) {
        return 1;
      }
      if (a.available > b.available) {
        return -1;
      }
      return 0;
    }
    if (isAvailableASC) {
      const newReport = reports.sort(conpareAvailableASC);
      setReports(newReport);
    } else {
      const newReport = reports.sort(conpareAvailableDES);
      setReports(newReport);
    }
    setIsAvailableASC(!isAvailableASC);
  };
  const handleSortNotAvailable = () => {
    function conpareNotAvailableASC(a, b) {
      if (a.notAvailable < b.notAvailable) {
        return -1;
      }
      if (a.notAvailable > b.notAvailable) {
        return 1;
      }
      return 0;
    }
    function conpareNotAvailableDES(a, b) {
      if (a.notAvailable < b.notAvailable) {
        return 1;
      }
      if (a.notAvailable > b.notAvailable) {
        return -1;
      }
      return 0;
    }
    if (isNotAvailableASC) {
      const newReport = reports.sort(conpareNotAvailableASC);
      setReports(newReport);
    } else {
      const newReport = reports.sort(conpareNotAvailableDES);
      setReports(newReport);
    }
    setIsNotAvailableASC(!isNotAvailableASC);
  };
  const handleSortByWaiting = () => {
    function conpareWaitingForRecycleASC(a, b) {
      if (a.waitingForRecycle < b.waitingForRecycle) {
        return -1;
      }
      if (a.waitingForRecycle > b.waitingForRecycle) {
        return 1;
      }
      return 0;
    }
    function conpareWaitingForRecycleDES(a, b) {
      if (a.waitingForRecycle < b.waitingForRecycle) {
        return 1;
      }
      if (a.waitingForRecycle > b.waitingForRecycle) {
        return -1;
      }
      return 0;
    }
    if (isWaitingASC) {
      const newReport = reports.sort(conpareWaitingForRecycleASC);
      setReports(newReport);
    } else {
      const newReport = reports.sort(conpareWaitingForRecycleDES);
      setReports(newReport);
    }
    setIsWaitingASC(!isWaitingASC);
  };
  const handleSortByRecycled = () => {
    function conpareRecycledASC(a, b) {
      if (a.recycled < b.recycled) {
        return -1;
      }
      if (a.recycled > b.recycled) {
        return 1;
      }
      return 0;
    }
    function conpareRecycledDES(a, b) {
      if (a.recycled < b.recycled) {
        return 1;
      }
      if (a.recycled > b.recycled) {
        return -1;
      }
      return 0;
    }
    if (isRecycledASC) {
      const newReport = reports.sort(conpareRecycledASC);
      setReports(newReport);
    } else {
      const newReport = reports.sort(conpareRecycledDES);
      setReports(newReport);
    }
    setIsRecycledASC(!isRecycledASC);
  };
  useEffect(() => {
    const fetchReport = async () => {
      const url = "asset/report";
      const response = await get(url);
      setReports(response.data.data);
    };
    fetchReport();
  }, []);

  const handleExportReport = async () => {};
  return (
    <div id="report">
      <h4 id="title">
        <b>Report</b>
      </h4>
      <div id="button_zone">
        <Export csvData={reports} fileName={"AssetReport"} />
      </div>
      <table>
        <thead>
          <tr>
            <th className="head-title" onClick={handleSortByCategory}>
              Category
              {isCategoryASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th className="head-title" onClick={handleSortByTotal}>
              Total
              {isTotalASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th className="head-title" onClick={handleSortByAssigned}>
              Assigned
              {isAssignedASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th className="head-title" onClick={handleSortByAvailable}>
              Available
              {isAvailableASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th className="head-title" onClick={handleSortNotAvailable}>
              Not Available
              {isNotAvailableASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th className="head-title" onClick={handleSortByWaiting}>
              Waiting for recyling
              {isWaitingASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th className="head-title" onClick={handleSortByRecycled}>
              Recyled
              {isRecycledASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
          </tr>
        </thead>

        <tbody>
          {reports.map((report) => {
            return <Report report={report} />;
          })}
        </tbody>
      </table>
    </div>
  );
};
export default Reports;
