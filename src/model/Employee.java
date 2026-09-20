package model;

public class Employee {
    private int employeeId;
    private int userId;
    private String name;
    private String email;
    private String department;
    private String designation;

    private String employeeCode;
    private String dob;
    private String phone;
    private String personalEmail;
    private String emergencyContact;
    private String currentAddress;
    private String permanentAddress;
    private String employmentType;
    private String workLocation;
    private String currentSalary;
    private String firstSalary;
    private String pfEsiAmount;
    private String attendancePercentage;
    private String joiningDate;

    public Employee() {}

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public String getCurrentAddress() { return currentAddress; }
    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }
    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public String getWorkLocation() { return workLocation; }
    public void setWorkLocation(String workLocation) { this.workLocation = workLocation; }
    public String getCurrentSalary() { return currentSalary; }
    public void setCurrentSalary(String currentSalary) { this.currentSalary = currentSalary; }
    public String getFirstSalary() { return firstSalary; }
    public void setFirstSalary(String firstSalary) { this.firstSalary = firstSalary; }
    public String getPfEsiAmount() { return pfEsiAmount; }
    public void setPfEsiAmount(String pfEsiAmount) { this.pfEsiAmount = pfEsiAmount; }
    public String getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(String attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    public String getJoiningDate() { return joiningDate; }
    public void setJoiningDate(String joiningDate) { this.joiningDate = joiningDate; }
}