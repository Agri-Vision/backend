package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.port.in.StressUseCase;

public class StressUseCaseImpl implements StressUseCase {
    @Override
    public String getStressByTileId(Tile tile) {
        if (tile.getStress() == null) tile.setStress("");
        if (!"yes".equals(tile.getStress())) {
            return "No Stress Detected";
        }

        return identifyWaterStressAndSuggestions(tile);
    }

    private String identifyWaterStressAndSuggestions(Tile tile) {
        double soilMoisture = tile.getSoilMoisture();
        double uvIndex = tile.getUvLevel();
        double temperature = tile.getTemperature();
        double humidity = tile.getHumidity();

        if (isWaterStressDetected(soilMoisture, temperature, humidity)) {
            String suggestion = generateSuggestions(soilMoisture, uvIndex);
            return "Possible Water Stress Detected. " + suggestion;
        }

        return "Unknown or Non-Water Stress Type";
    }

    private boolean isWaterStressDetected(double soilMoisture, double temperature, double humidity) {
        return soilMoisture < 300.0 && temperature > 30.0 && humidity < 40.0;
    }

    private String generateSuggestions(double soilMoisture, double uvIndex) {
        StringBuilder suggestion = new StringBuilder();

        if (soilMoisture < 30.0) {
            double requiredIncrease = 30.0 - soilMoisture;
            suggestion.append("Increase soil moisture by ").append(requiredIncrease).append("% through additional irrigation. ");
        }

        if (uvIndex > 5.0) {
            double requiredReduction = uvIndex - 5.0;
            suggestion.append("Reduce UV exposure by using shade cloth or protective coverings with approximately ")
                    .append((requiredReduction / uvIndex) * 100).append("% shading.");
        }

        return suggestion.toString();
    }
}
