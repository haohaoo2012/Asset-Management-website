package com.nashtech.rootkies.constants;

public class State {
    // Asset
    public static final Short AVAILABLE = 1;
    public static final Short NOT_AVAILABLE = 2;
    public static final Short ASSIGNED = 3;
    public static final Short WAITING_FOR_RECYCLING = 4;
    public static final Short RECYLED = 5;

    // Assignment
    public static final Short ACCEPTED = 1;
    public static final Short WAITING_FOR_ACCEPTANCE = 2;
    public static final Short DECLINED = 3;
    public static final Short ASSIGNMENT_HAD_COMPLETED_ASSET_HAD_RETURNED = 4;

    // Request
    public static final Short WAITING_FOR_RETURNING = 1;
    public static final Short COMPLETED = 2;
}
