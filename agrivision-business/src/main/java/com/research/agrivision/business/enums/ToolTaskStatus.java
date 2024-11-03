package com.research.agrivision.business.enums;

public enum ToolTaskStatus {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    ERROR("ERROR"),
    UNIDENTIFIED("UNIDENTIFIED"),
    NULL_TASK("NULL_TASK");

    ToolTaskStatus(String name){this.name = name;}
    private final String name;

    public static ToolTaskStatus fromString(String status) {
        for (ToolTaskStatus taskStatus : ToolTaskStatus.values()) {
            if (taskStatus.toString().equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        return null;
    }
}
