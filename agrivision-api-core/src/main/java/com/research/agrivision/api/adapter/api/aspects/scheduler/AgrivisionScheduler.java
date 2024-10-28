package com.research.agrivision.api.adapter.api.aspects.scheduler;

import com.research.agrivision.business.entity.IotReading;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.entity.ToolProject;
import com.research.agrivision.business.entity.imageTool.Response.TaskStatusResponse;
import com.research.agrivision.business.enums.ToolTaskStatus;
import com.research.agrivision.business.port.out.GetIotPort;
import com.research.agrivision.business.port.out.SaveTilePort;
import com.research.agrivision.business.port.out.ToolPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@EnableScheduling
public class AgrivisionScheduler {
    List<Double> temperatures = List.of(37.90, 33.50, 34.70, 36.70, 37.00);
    List<Double> humidities = List.of(60.50, 59.60, 64.30, 54.60, 51.80);
    List<Double> uvLevels = List.of(1.54, 1.60, 1.41, 1.73, 2.06);
    List<Double> soilMoistures = List.of(502.00, 499.00, 452.00, 516.00, 514.00);
    List<Double> pressures = List.of(93730.00, 93851.00, 93801.00, 93811.00, 93773.00);
    List<Double> altitudes = List.of(666.00, 655.00, 658.00, 659.00, 662.00);

    @Autowired
    private ToolPort toolPort;

    @Autowired
    private SaveTilePort saveTilePort;

    @Autowired
    private GetIotPort getIotPort;

    @Value("${scheduler.enabled}")
    private boolean isEnabled;

    private static final Logger logger = LoggerFactory.getLogger(AgrivisionScheduler.class);

    private final Random random = new Random();

    IotReading iotReading = getIotPort.getLatestReading();

    @Scheduled(fixedDelay = 60000)
    public void processToolProjects() {
        List<ToolProject> toolProjectList = toolPort.getAllToolProjectsByStatus(ToolTaskStatus.PENDING);

        for (ToolProject toolProject : toolProjectList) {
            try {
                if (toolProject.getTask() == null) {
                    toolPort.updateToolProjectStatus(toolProject.getId(), ToolTaskStatus.NULL_TASK);
                    continue;
                }

                TaskStatusResponse response = toolPort.getTaskStatus(toolProject.getToolTaskId());

                if ("PENDING".equalsIgnoreCase(response.getState())){
                    continue;
                } else if ("FAILURE".equalsIgnoreCase(response.getState())) {
                    toolPort.updateToolProjectStatus(toolProject.getId(), ToolTaskStatus.FAILED);
                    continue;
                } else if ("SUCCESS".equalsIgnoreCase(response.getState())) {
                    toolPort.updateToolProjectStatus(toolProject.getId(), ToolTaskStatus.PROCESSING);
                } else {
                    toolPort.updateToolProjectStatus(toolProject.getId(), ToolTaskStatus.UNIDENTIFIED);
                    continue;
                }

                List<Tile> tiles = new ArrayList<>();

                response.getResult().getPatches().forEach(patch -> {
                    Tile tile = new Tile();

                    tile.setNdvi(patch.getNdvi().getMean());
                    tile.setRendvi(patch.getRendvi().getMean());
                    tile.setCire(patch.getCire().getMean());
                    tile.setPri(patch.getPri().getMean());

                    tile.setRow(patch.getRow());
                    tile.setCol(patch.getColumn());

                    tile.setRowCol(patch.getRow() + "_" + patch.getColumn());

//                    tile.setTemperature(getRandomValue(temperatures));
//                    tile.setHumidity(getRandomValue(humidities));
//                    tile.setUvLevel(getRandomValue(uvLevels));
//                    tile.setSoilMoisture(getRandomValue(soilMoistures));
//                    tile.setPressure(getRandomValue(pressures));
//                    tile.setAltitude(getRandomValue(altitudes));

                    tile.setTemperature(Double.valueOf(iotReading.getTemperature()));
                    tile.setHumidity(Double.valueOf(iotReading.getHumidity()));
                    tile.setUvLevel(Double.valueOf(iotReading.getUvLevel()));
                    tile.setSoilMoisture(Double.valueOf(iotReading.getSoilMoisture()));
                    tile.setPressure(Double.valueOf(iotReading.getPressure()));
                    tile.setAltitude(Double.valueOf(iotReading.getAltitude()));

                    tiles.add(tile);
                });

                saveTilePort.createSchedulerTileListByTask(toolProject.getTask().getId(), tiles);
                toolPort.updateToolProjectStatus(toolProject.getId(), ToolTaskStatus.SUCCESS);

            } catch (Exception e) {
                toolPort.updateToolProjectStatus(toolProject.getId(), ToolTaskStatus.ERROR);
                logger.error("Failed to process Tool Project with id: " + toolProject.getId() + " with Tool Task Id: " + toolProject.getToolTaskId(), e);
            }
        }
    }

    private Double getRandomValue(List<Double> values) {
        return values.get(random.nextInt(values.size()));
    }
}
