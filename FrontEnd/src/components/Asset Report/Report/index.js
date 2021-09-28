import React from "react";
import "./rp.css";
export const Report = ({ report }) => {
  return (
    <tr id={report.category} className="report-row">
      <td className="col">{report.category}</td>
      <td>&nbsp;</td>
      <td className="col">{report.total}</td>
      <td>&nbsp;</td>
      <td className="col">{report.assigned}</td>
      <td>&nbsp;</td>
      <td className="col">{report.available}</td>
      <td>&nbsp;</td>
      <td className="col">{report.notAvailable}</td>
      <td>&nbsp;</td>
      <td className="col">{report.waitingForRecycle}</td>
      <td>&nbsp;</td>
      <td className="col">{report.recycled}</td>
    </tr>
  );
};
