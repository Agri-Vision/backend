package com.research.agrivision.business.enums;

public enum TaskType {
    RGB("RGB"),
    R("R"),
    G("G"),
    RE("RE"),
    NIR("NIR");

    TaskType(String name){this.name = name;}
    private final String name;

    public static TaskType fromString(String taskType) {
        for (TaskType type : TaskType.values()) {
            if (type.toString().equalsIgnoreCase(taskType)) {
                return type;
            }
        }
        return null;
    }
}
