package com.nashtech.rootkies.dto.asset.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {
    String category;
    int total;
    int assigned;
    int available;
    int notAvailable;
    int waitingForRecycle;
    int recycled;

    public void countTotal(){
        setTotal(this.assigned + this.available + this.notAvailable + this.waitingForRecycle + this.recycled);
    }

}
