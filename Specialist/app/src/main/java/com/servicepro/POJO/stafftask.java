package com.servicepro.POJO;

public class stafftask {

    private String TaskAssignId;
    private String Requirement_Id;
    private String 	Staff_id;
    private String Date;
    private String Status;
    private String msg;
    private String result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTaskAssignId() {
        return TaskAssignId;
    }

    public void setTaskAssignId(String taskAssignId) {
        TaskAssignId = taskAssignId;
    }

    public String getRequirement_Id() {
        return Requirement_Id;
    }

    public void setRequirement_Id(String requirement_Id) {
        Requirement_Id = requirement_Id;
    }

    public String getStaff_id() {
        return Staff_id;
    }

    public void setStaff_id(String staff_id) {
        Staff_id = staff_id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
