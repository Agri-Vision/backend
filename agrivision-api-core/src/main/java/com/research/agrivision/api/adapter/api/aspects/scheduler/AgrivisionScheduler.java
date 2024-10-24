package com.research.agrivision.api.adapter.api.aspects.scheduler;

import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.entity.ToolProject;
import com.research.agrivision.business.entity.imageTool.Response.TaskStatusResponse;
import com.research.agrivision.business.enums.ToolTaskStatus;
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
    List<Double> temperatures = List.of(30.70, 25.72, 23.75, 32.78, 34.80);
    List<Double> humidities = List.of(76.45, 74.50, 72.51, 80.55, 70.60);
    List<Double> uvLevels = List.of(0.70, 0.62, 0.65, 0.74, 0.78);
    List<Double> soilMoistures = List.of(260.45, 271.50, 251.52, 281.55, 268.60);
    List<Double> pressures = List.of(93030.00, 90035.00, 95037.14, 97040.00, 98045.00);
    List<Double> altitudes = List.of(450.00, 351.00, 551.42, 402.00, 653.00);

    @Autowired
    private ToolPort toolPort;

    @Autowired
    private SaveTilePort saveTilePort;

    @Value("${scheduler.enabled}")
    private boolean isEnabled;

    private static final Logger logger = LoggerFactory.getLogger(AgrivisionScheduler.class);

    private final Random random = new Random();

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

                    tile.setTemperature(getRandomValue(temperatures));
                    tile.setHumidity(getRandomValue(humidities));
                    tile.setUvLevel(getRandomValue(uvLevels));
                    tile.setSoilMoisture(getRandomValue(soilMoistures));
                    tile.setPressure(getRandomValue(pressures));
                    tile.setAltitude(getRandomValue(altitudes));

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
