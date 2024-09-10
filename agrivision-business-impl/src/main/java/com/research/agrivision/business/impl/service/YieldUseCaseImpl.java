package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Plantation;
import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.port.in.YieldUseCase;
import com.research.agrivision.business.port.out.GetPlantationPort;
import com.research.agrivision.business.port.out.GetProjectPort;
import com.research.agrivision.business.port.out.GetTilePort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class YieldUseCaseImpl implements YieldUseCase {
    @Autowired
    private GetTilePort getTilePort;
    @Autowired
    private GetPlantationPort getPlantationPort;
    @Autowired
    private GetProjectPort getProjectPort;

    @Override
    public String validateTile(Tile tile, Plantation plantation) {
        List<Tile> tileList = new ArrayList<>();
        List<Project> projectList = getProjectPort.getAllProjectsByPlantationId(plantation.getId());

        if (projectList == null || projectList.isEmpty()) return "N/A";

        for (Project project : projectList) {
            if (project.getTaskList() == null || project.getTaskList().isEmpty()) continue;
            List<Task> taskList = project.getTaskList();
            for (Task task : taskList) {
                if (task.getTileList() == null || task.getTileList().isEmpty()) continue;
                tileList.addAll(task.getTileList());
            }
        }

        if (tileList.isEmpty()) return "N/A";

        return analyseTile(tile, tileList);
    }

    private String analyseTile(Tile tile, List<Tile> tileList) {
        double maxDeviation = 0.0;
        String mostLikelyFactor = "Average conditions.";

        double avgTemperature = calculateAverageTemperature(tileList);
        double temperatureDeviation = Math.abs(tile.getTemperature() - avgTemperature);
        if (temperatureDeviation > maxDeviation) {
            maxDeviation = temperatureDeviation;
            mostLikelyFactor = "Possible high temperature affecting yield.";
        }

        double avgHumidity = calculateAverageHumidity(tileList);
        double humidityDeviation = Math.abs(tile.getHumidity() - avgHumidity);
        if (humidityDeviation > maxDeviation) {
            maxDeviation = humidityDeviation;
            mostLikelyFactor = "Possible low humidity affecting yield.";
        }

        double avgUVLevel = calculateAverageUVLevel(tileList);
        double uvLevelDeviation = Math.abs(tile.getUvLevel() - avgUVLevel);
        if (uvLevelDeviation > maxDeviation) {
            maxDeviation = uvLevelDeviation;
            mostLikelyFactor = "Possible high UV level affecting yield.";
        }

        double avgSoilMoisture = calculateAverageSoilMoisture(tileList);
        double soilMoistureDeviation = Math.abs(tile.getSoilMoisture() - avgSoilMoisture);
        if (soilMoistureDeviation > maxDeviation) {
            maxDeviation = soilMoistureDeviation;
            mostLikelyFactor = "Possible low soil moisture affecting yield.";
        }

        double acceptableDeviationThreshold = 5.0;

        if (maxDeviation <= acceptableDeviationThreshold) {
            return "Average conditions for tile ID: " + tile.getId();
        }

        return mostLikelyFactor + " for tile ID: " + tile.getId();
    }

    private double calculateAverageTemperature(List<Tile> tileList) {
        return tileList.stream()
                .mapToDouble(Tile::getTemperature)
                .average()
                .orElse(0);
    }

    private double calculateAverageHumidity(List<Tile> tileList) {
        return tileList.stream()
                .mapToDouble(Tile::getHumidity)
                .average()
                .orElse(0);
    }

    private double calculateAverageUVLevel(List<Tile> tileList) {
        return tileList.stream()
                .mapToDouble(Tile::getUvLevel)
                .average()
                .orElse(0);
    }

    private double calculateAverageSoilMoisture(List<Tile> tileList) {
        return tileList.stream()
                .mapToDouble(Tile::getSoilMoisture)
                .average()
                .orElse(0);
    }
}
