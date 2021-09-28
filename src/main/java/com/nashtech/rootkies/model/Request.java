package com.nashtech.rootkies.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests",
        indexes = {
                @Index(name = "request_requestedby_idx" , columnList = "requestedby"),
                @Index(name = "request_returneddate_idx" , columnList = "returneddate"),
                @Index(name = "request_state_idx" , columnList = "state"),
                @Index(name = "request_assignmentid_idx" , columnList = "assignmentid")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    @Id
    @GeneratedValue
    @Column(name = "requestid")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "requestedby", referencedColumnName = "staffcode", nullable = false)
    private User requestedBy;

    @ManyToOne
    @JoinColumn(name = "acceptedby", referencedColumnName = "staffcode")
    private User acceptedBy;

    @Column(name = "returneddate")
    private LocalDateTime returnedDate;

    @Column(name = "state")
    private Short state;

    @ManyToOne
    @JoinColumn(name = "assignmentid", nullable = false)
    private Assignment assignment;

    @Column(name = "isdeleted")
    private Boolean isDeleted;
}
