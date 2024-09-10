package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.port.in.DiseaseUseCase;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class DiseaseUseCaseImpl implements DiseaseUseCase {
    @Override
    public String getDiseaseScoreByTileId(Tile tile) {
        double alpha = 0.2, beta = 0.2, gamma = 0.2, delta = 0.2, eta = 0.1, zeta = 0.1;

        double ndvi = normalize(tile.getNdvi(), 0, 1);
        double reNdvi = normalize(tile.getRendvi(), 0, 1);
        double ciRe = normalize(tile.getCire(), 0, 1);
        double pri = normalize(tile.getPri(), 0, 1);
        double temperature = normalize(tile.getTemperature(), 10, 40);
        double soilMoisture = normalize(tile.getSoilMoisture(), 0, 100);

        double tempFactor = 1 - (temperature / 40);

        double healthScore = alpha * ndvi +
                beta * reNdvi +
                gamma * ciRe +
                delta * pri +
                eta * tempFactor +
                zeta * soilMoisture;

        return classifyTile(healthScore);
    }

    private double normalize(double value, double min, double max) {
        if (value < min) return 0;
        if (value > max) return 1;
        return (value - min) / (max - min);
    }

    public String classifyTile(double healthScore) {
        double healthScorePct = healthScore * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedPct = df.format(healthScorePct);
        healthScorePct = Double.parseDouble(formattedPct);

        if (healthScore > 0.75) {
            return healthScorePct + "%" + " - " + "Not Vulnerable to Diseases";
        } else if (healthScore >= 0.5) {
            return healthScorePct + "%" + " - " + "Mildly Vulnerable to Diseases";
        } else if (healthScore >= 0.25){
            return healthScorePct + "%" + " - " + "Vulnerable to Diseases";
        } else {
            return healthScorePct + "%" + " - " + "Severely Vulnerable to Diseases";
        }
    }
}
