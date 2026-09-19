package model;

public class Resignation {
    private int id;
    private int employeeId;
    private String employeeName;
    private String reason;
    private String lastWorkingDay;
    private String status;
    private String appliedDate;
    private boolean assetsReturned;
    private boolean pendingTasksCompleted;
    private boolean documentsCleared;
    private boolean hrReviewCompleted;
    private boolean exitInterviewCompleted;
    private boolean relievingLetterIssued;
    private boolean exitCompleted;

    public Resignation() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getLastWorkingDay() { return lastWorkingDay; }
    public void setLastWorkingDay(String lastWorkingDay) { this.lastWorkingDay = lastWorkingDay; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAppliedDate() { return appliedDate; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }
    public boolean isAssetsReturned() { return assetsReturned; }
    public void setAssetsReturned(boolean assetsReturned) { this.assetsReturned = assetsReturned; }
    public boolean isPendingTasksCompleted() { return pendingTasksCompleted; }
    public void setPendingTasksCompleted(boolean pendingTasksCompleted) { this.pendingTasksCompleted = pendingTasksCompleted; }
    public boolean isDocumentsCleared() { return documentsCleared; }
    public void setDocumentsCleared(boolean documentsCleared) { this.documentsCleared = documentsCleared; }
    public boolean isHrReviewCompleted() { return hrReviewCompleted; }
    public void setHrReviewCompleted(boolean hrReviewCompleted) { this.hrReviewCompleted = hrReviewCompleted; }
    public boolean isExitInterviewCompleted() { return exitInterviewCompleted; }
    public void setExitInterviewCompleted(boolean exitInterviewCompleted) { this.exitInterviewCompleted = exitInterviewCompleted; }
    public boolean isRelievingLetterIssued() { return relievingLetterIssued; }
    public void setRelievingLetterIssued(boolean relievingLetterIssued) { this.relievingLetterIssued = relievingLetterIssued; }
    public boolean isExitCompleted() { return exitCompleted; }
    public void setExitCompleted(boolean exitCompleted) { this.exitCompleted = exitCompleted; }
}