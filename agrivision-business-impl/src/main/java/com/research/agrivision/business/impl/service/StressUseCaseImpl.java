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
        return soilMoisture < 510.0 && temperature > 33.0 && humidity < 65.60;
    }

    private String generateSuggestions(double soilMoisture, double uvIndex) {
        StringBuilder suggestion = new StringBuilder();

        if (soilMoisture < 510.0) {
            long requiredIncrease = Math.round(510.0f - soilMoisture);
            suggestion.append("Increase soil moisture by ").append(requiredIncrease).append("% through additional irrigation. ");
        }

        if (uvIndex > 1.0) {
            long requiredReduction = Math.round(uvIndex - 1.0);
            suggestion.append("Reduce UV exposure by using shade cloth or protective coverings with approximately ")
                    .append((requiredReduction / uvIndex) * 100).append("% shading.");
        }

        return suggestion.toString();
    }
}
