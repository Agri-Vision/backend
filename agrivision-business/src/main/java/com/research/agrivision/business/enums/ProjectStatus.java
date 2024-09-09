package com.research.agrivision.business.enums;

public enum ProjectStatus {
    CREATED("Created"),
    PENDING("Pending"),
    COMPLETED("Completed");

    ProjectStatus(String name){this.name = name;}
    private final String name;

    public static ProjectStatus fromString(String projectStatus) {
        for (ProjectStatus type : ProjectStatus.values()) {
            if (type.toString().equalsIgnoreCase(projectStatus)) {
                return type;
            }
        }
        return null;
    }
}
