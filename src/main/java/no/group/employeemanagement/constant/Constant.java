package no.group.employeemanagement.constant;

public class Constant {

    public static final String ADMIN_REQUEST_PROCESS = "3000";
    public static final String EMPLOYEE_REQUEST_PROCESS = "2000";
    public static final String LEAVE_REQUEST_PROCESS = "1000";
    public static final String DATA_NOT_FOUND = "4004";
    public static String getDataNotFoundMessage(String processCode) {
        return "Data Not Found in the " + processCode + " for id: ";
    }
}
