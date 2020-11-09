package no.kristiania.database.objects;

public class MemberToProject {

    private String status;
    private String projectMemberName;
    private String projectName;
    private Long id;
    private String taskName;
    private String description;
    private int projectId;
    private int taskId;
    private String firstName;
    private String lastName;
    private int memberNameId;
    private int statusId;

    //GETTERS

    public String getStatus() {
        return status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getMemberNameId() {
        return memberNameId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getStatusId() {
        return statusId;
    }

    //SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setProjectMemberFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setProjectMemberLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNameId(int memberNameId) {
        this.memberNameId = memberNameId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
