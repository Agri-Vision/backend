package com.research.agrivision.business.enums;

public enum SampleType {
    TYPE_A("Type A"),
    TYPE_B("Type B");

    SampleType(String name){this.name = name;}
    private final String name;

    public static SampleType fromString(String sampleType) {
        for (SampleType type : SampleType.values()) {
            if (type.toString().equalsIgnoreCase(sampleType)) {
                return type;
            }
        }
        return null;
    }
}
