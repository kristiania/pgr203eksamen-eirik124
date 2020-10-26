package no.kristiania.database;

public class ProjectMemberToProject {

    private String status;
    private String projectMemberName;
    private String projectName;
    private Long id;
    private String taskName;

    //GETTERS

    public String getStatus() {
        return status;
    }

    public String getProjectMemberName() {
        return projectMemberName;
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


    //SETTERS


    public void setId(Long id) {
        this.id = id;
    }

    public void setProjectMemberName(String projectMemberName) {
        this.projectMemberName = projectMemberName;
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
}
