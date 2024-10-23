package com.research.agrivision.api.adapter.api.aspects.scheduler;

import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.entity.ToolProject;
import com.research.agrivision.business.entity.imageTool.Response.TaskStatusResponse;
import com.research.agrivision.business.enums.ToolTaskStatus;
import com.research.agrivision.business.port.in.ProjectUseCase;
import com.research.agrivision.business.port.in.ToolUseCase;
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
    List<Double> temperatures = List.of(30.70, 30.72, 30.75, 30.78, 30.80);
    List<Double> humidities = List.of(76.45, 76.50, 76.51, 76.55, 76.60);
    List<Double> uvLevels = List.of(0.70, 0.72, 0.74, 0.76, 0.78);
    List<Double> soilMoistures = List.of(271.45, 271.50, 271.52, 271.55, 271.60);
    List<Double> pressures = List.of(95030.00, 95035.00, 95037.14, 95040.00, 95045.00);
    List<Double> altitudes = List.of(550.00, 551.00, 551.42, 552.00, 553.00);

    @Autowired
    private ProjectUseCase projectUseCase;

    @Autowired
    private ToolUseCase toolUseCase;

    @Autowired
    private ToolPort toolPort;

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
