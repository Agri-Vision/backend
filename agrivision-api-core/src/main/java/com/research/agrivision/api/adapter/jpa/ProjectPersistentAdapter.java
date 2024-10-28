package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.jpa.entity.*;
import com.research.agrivision.api.adapter.jpa.repository.*;
import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.entity.imageTool.ToolReadings;
import com.research.agrivision.business.entity.ml.sample.DiseaseRequest;
import com.research.agrivision.business.entity.project.ProjectHistory;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.agrivision.business.enums.TaskType;
import com.research.agrivision.business.port.out.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProjectPersistentAdapter implements GetProjectPort, GetTaskPort, GetTilePort, SaveProjectPort, SaveTaskPort, SaveTilePort {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TileRepository tileRepository;
    private final AvUserRepository userRepository;
    private final PlantationRepository plantationRepository;

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private MlPort mlPort;

    public ProjectPersistentAdapter(ProjectRepository projectRepository, TaskRepository taskRepository, TileRepository tileRepository, AvUserRepository userRepository, PlantationRepository plantationRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.tileRepository = tileRepository;
        this.userRepository = userRepository;
        this.plantationRepository = plantationRepository;
    }

    @Override
    public Project createProject(Project project) {
        if (project == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.Project dbProject = mapper.map(project, com.research.agrivision.api.adapter.jpa.entity.Project.class);
        if (project.getAgent() != null) {
            AvUser agent = userRepository.findById(project.getAgent().getId()).orElse(null);
            dbProject.setAgent(agent);
        }
        if (project.getPlantation() != null) {
            Plantation plantation = plantationRepository.findById(project.getPlantation().getId()).orElse(null);
            dbProject.setPlantation(plantation);
        }
        projectRepository.save(dbProject);
        return mapper.map(dbProject, Project.class);
    }

    @Override
    public Project updateProject(Project request) {
        if (request == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.Project dbProject = mapper.map(request, com.research.agrivision.api.adapter.jpa.entity.Project.class);
        if (dbProject.getTaskList() != null) {
            for (Task task : dbProject.getTaskList()) {
                task.setProject(dbProject);
                if (task.getTileList() == null) continue;
                for (Tile tile : task.getTileList()) {
                    tile.setTask(task);
                }
            }
        }
        if (dbProject.getIotDeviceList() != null) {
            for (IotDevice iotDevice : dbProject.getIotDeviceList()) {
                iotDevice.setProject(dbProject);
            }
        }
        if (request.getAgent() != null) {
            AvUser agent = userRepository.findById(request.getAgent().getId()).orElse(null);
            dbProject.setAgent(agent);
        }
        if (request.getPlantation() != null) {
            Plantation plantation = plantationRepository.findById(request.getPlantation().getId()).orElse(null);
            dbProject.setPlantation(plantation);
        }
        projectRepository.save(dbProject);
        return mapper.map(dbProject, com.research.agrivision.business.entity.Project.class);
    }

    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Project getProjectById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return mapper.map(project, com.research.agrivision.business.entity.Project.class);
        }
        return null;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Project::getLastModifiedDate).reversed())
                .map(project -> mapper.map(project, com.research.agrivision.business.entity.Project.class))
                .toList();
    }

    @Override
    public List<Project> getAllProjectsByPlantationId(Long id) {
        return projectRepository.findAllByPlantationId(id).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Project::getLastModifiedDate).reversed())
                .map(project -> mapper.map(project, com.research.agrivision.business.entity.Project.class))
                .toList();
    }

    @Override
    public Project getProjectByWebOdmProjectId(String projectId) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Project> project = projectRepository.findByWebOdmProjectId(projectId);
        if (project.isPresent()) {
            return mapper.map(project, com.research.agrivision.business.entity.Project.class);
        }
        return null;
    }

    @Override
    public List<Project> getAllProjectsByStatus(ProjectStatus status) {
        return projectRepository.findAllByStatus(status).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Project::getLastModifiedDate).reversed())
                .map(project -> mapper.map(project, com.research.agrivision.business.entity.Project.class))
                .toList();
    }

    @Override
    public List<Project> getAllProjectsByAgent(Long id) {
        return projectRepository.findAllByAgentId(id).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Project::getLastModifiedDate).reversed())
                .map(project -> mapper.map(project, com.research.agrivision.business.entity.Project.class))
                .toList();
    }

    @Override
    public List<ProjectHistory> getProjectHistoryByPlantationId(Long id) {
        List<com.research.agrivision.api.adapter.jpa.entity.Project> projects = projectRepository.findAllByPlantationIdAndStatus(id, ProjectStatus.COMPLETED);

        return projects.stream().map(project -> {
            AvUser agent = null;
            if (project.getAgent() != null && project.getAgent().getId() != null) {
                agent = userRepository.findById(project.getAgent().getId()).orElse(null);
            }

            ProjectHistory projectHistory = new ProjectHistory();
            projectHistory.setProjectCompletedDate(project.getLastModifiedDate());
            projectHistory.setProjectCreatedDate(project.getCreatedDate());
            projectHistory.setProjectId(project.getId());
            projectHistory.setProjectName(project.getProjectName());
            projectHistory.setAgent(mapper.map(agent, User.class));

            Optional<Task> optionalTask = project.getTaskList().stream()
                    .filter(task -> task.getTaskType() == TaskType.RGB)
                    .findFirst();

            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();

                List<Tile> tiles = task.getTileList();
                if (tiles == null || tiles.isEmpty()) {
                    projectHistory.setTotalYield(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
                    projectHistory.setStressPct(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
                    projectHistory.setDiseasePct(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
                } else {
                    BigDecimal totalYield = tiles.stream()
                            .map(tile -> new BigDecimal(tile.getYield()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    projectHistory.setTotalYield(totalYield.setScale(2, RoundingMode.DOWN));

                    long stressedTiles = tiles.stream()
                            .filter(tile -> "yes".equalsIgnoreCase(tile.getStress()))
                            .count();
                    long totalTiles = tiles.size();
                    BigDecimal stressPct = BigDecimal.valueOf(stressedTiles * 100.0 / totalTiles).setScale(2, RoundingMode.DOWN);
                    projectHistory.setStressPct(stressPct.setScale(2, RoundingMode.DOWN));

                    long diseasedTiles = tiles.stream()
                            .filter(tile -> "yes".equalsIgnoreCase(tile.getDisease()))
                            .count();
                    BigDecimal diseasePct = BigDecimal.valueOf(diseasedTiles * 100.0 / totalTiles).setScale(2, RoundingMode.DOWN);
                    projectHistory.setDiseasePct(diseasePct.setScale(2, RoundingMode.DOWN));
                }
            } else {
                projectHistory.setTotalYield(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
                projectHistory.setStressPct(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
                projectHistory.setDiseasePct(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
            }

            return projectHistory;
        }).collect(Collectors.toList());
    }

    @Override
    public long getProjectCountByPlantationId(Long id) {
        return projectRepository.countByPlantationIdAndStatus(id, ProjectStatus.COMPLETED);
    }

    @Override
    public com.research.agrivision.business.entity.Task getTaskByWebOdmTaskId(String taskId) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Task> task = taskRepository.findByWebOdmTaskId(taskId);
        if (task.isPresent()) {
            return mapper.map(task, com.research.agrivision.business.entity.Task.class);
        }
        return null;
    }

    @Override
    public com.research.agrivision.business.entity.Task getTaskById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return mapper.map(task, com.research.agrivision.business.entity.Task.class);
        }
        return null;
    }

    @Override
    public void createTile(ToolReadings toolReadings) {
        Tile tile = new Tile();
        com.research.agrivision.api.adapter.jpa.entity.Task task = taskRepository.findByWebOdmTaskId(toolReadings.getTaskId()).orElse(null);

        if (task != null && task.getProject() !=null && task.getProject().getId() != null) {
            com.research.agrivision.api.adapter.jpa.entity.Project project = projectRepository.findById(task.getProject().getId()).orElse(null);
            if (project != null) {
                project.setStatus(ProjectStatus.COMPLETED);
                projectRepository.save(project);
            }
        }

        tile.setTask(task);
        tile.setNdvi(toolReadings.getNdvi());
        tile.setRendvi(toolReadings.getRendvi());
        tile.setCire(toolReadings.getCire());
        tile.setPri(toolReadings.getPri());
        tile.setTemperature(30.7571428);
        tile.setHumidity(76.51428);
        tile.setUvLevel(0.7442);
        tile.setSoilMoisture(271.5714);
        tile.setPressure(95037.1428571);
        tile.setAltitude(551.4285714);

        DiseaseRequest stressRequest = new DiseaseRequest();
        ArrayList<Double> stressFeatures = new ArrayList<>();
        stressFeatures.add(tile.getRendvi());
        stressFeatures.add(tile.getTemperature());
        stressFeatures.add(tile.getHumidity());
        stressFeatures.add(tile.getUvLevel());
        stressFeatures.add(tile.getSoilMoisture());
        stressFeatures.add(tile.getPressure());
        stressFeatures.add(tile.getAltitude());

        stressRequest.setFeatures(stressFeatures);
        String stress = mlPort.getStressModel(stressRequest);
        tile.setStress(stress);

        DiseaseRequest diseaseRequest = new DiseaseRequest();
        ArrayList<Double> diseaseFeatures = new ArrayList<>();
        diseaseFeatures.add(tile.getNdvi());
        diseaseFeatures.add(tile.getRendvi());
        diseaseFeatures.add(tile.getCire());
        diseaseFeatures.add(tile.getPri());
        diseaseFeatures.add(tile.getTemperature());
        diseaseFeatures.add(tile.getHumidity());
        diseaseFeatures.add(tile.getUvLevel());

        diseaseRequest.setFeatures(diseaseFeatures);
        String disease = mlPort.getDiseaseModel(diseaseRequest);
        tile.setDisease(disease);

        DiseaseRequest yieldRequest = new DiseaseRequest();
        ArrayList<Double> yieldFeatures = new ArrayList<>();
        yieldFeatures.add(tile.getNdvi());
        yieldFeatures.add(tile.getTemperature());
        yieldFeatures.add(tile.getHumidity());
        yieldFeatures.add(tile.getUvLevel());
        yieldFeatures.add(tile.getSoilMoisture());
        yieldFeatures.add(tile.getPressure());
        yieldFeatures.add(tile.getAltitude());

        yieldRequest.setFeatures(yieldFeatures);
        String yield = mlPort.getYieldModel(yieldRequest);
        tile.setYield(yield);

        tileRepository.save(tile);
    }

    @Override
    public void createTilesByTaskId(Long id, List<com.research.agrivision.business.entity.Tile> tileList) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            List<Tile> dbTileList = tileList.stream()
                    .map(tile -> {
                        Tile dbTile = mapper.map(tile, Tile.class);
                        dbTile.setTask(task);
                        return dbTile;
                    })
                    .toList();

            tileRepository.saveAll(dbTileList);
        }
    }

    @Override
    public void createSchedulerTileListByTask(Long id, List<com.research.agrivision.business.entity.Tile> tileList) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            try {
                List<Tile> dbTileList = tileList.stream()
                        .map(tile -> {
                            Tile dbTile = mapper.map(tile, Tile.class);
                            dbTile.setTask(task);
                            setPredictions(dbTile);
                            return dbTile;
                        })
                        .toList();

                tileRepository.saveAll(dbTileList);
                if (task.getProject() != null && task.getProject().getId() != null) {
                    com.research.agrivision.api.adapter.jpa.entity.Project project = projectRepository.findById(task.getProject().getId()).orElse(null);
                    if (project != null) {
                        project.setStatus(ProjectStatus.COMPLETED);
                        projectRepository.save(project);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public com.research.agrivision.business.entity.Tile getTileById(Long tileId) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Tile> tile = tileRepository.findById(tileId);
        if (tile.isPresent()) {
            return mapper.map(tile, com.research.agrivision.business.entity.Tile.class);
        }
        return null;
    }

    @Override
    public List<com.research.agrivision.business.entity.Tile> getAllTiles() {
        return tileRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Tile::getLastModifiedDate).reversed())
                .map(tile -> mapper.map(tile, com.research.agrivision.business.entity.Tile.class))
                .toList();
    }

    @Override
    public List<com.research.agrivision.business.entity.Tile> getAllTilesByTaskId(Long id) {
        return tileRepository.findAllByTaskId(id).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Tile::getLastModifiedDate))
                .map(tile -> mapper.map(tile, com.research.agrivision.business.entity.Tile.class))
                .toList();
    }

    @Override
    public List<com.research.agrivision.business.entity.Tile> getAllTilesByProjectId(Long id) {
        return tileRepository.findAllByTaskProjectId(id).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Tile::getLastModifiedDate))
                .map(tile -> mapper.map(tile, com.research.agrivision.business.entity.Tile.class))
                .toList();
    }

    @Override
    public String getTotalYield() {
        List<Tile> tiles = tileRepository.findAll();
        if (tiles.isEmpty()) {
            return "0.00 Kg";
        } else {
            BigDecimal totalYield = tiles.stream()
                    .map(tile -> new BigDecimal(tile.getYield()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalYield = totalYield.setScale(2, RoundingMode.DOWN);
            return totalYield + " Kg";
        }
    }

    @Override
    public String getTotalStressPct() {
        List<Tile> tiles = tileRepository.findAll();
        if (tiles.isEmpty()) {
            return "0.00%";
        } else {
            long stressedTiles = tiles.stream()
                    .filter(tile -> "yes".equalsIgnoreCase(tile.getStress()))
                    .count();
            long totalTiles = tiles.size();

            BigDecimal stressPct = BigDecimal.valueOf(stressedTiles * 100.0 / totalTiles).setScale(2, RoundingMode.DOWN);
            stressPct = stressPct.setScale(2, RoundingMode.DOWN);

            return stressPct + "%";
        }
    }

    @Override
    public String getTotalDiseasePct() {
        List<Tile> tiles = tileRepository.findAll();
        if (tiles.isEmpty()) {
            return "0.00%";
        } else {

            long diseasedTiles = tiles.stream()
                    .filter(tile -> "yes".equalsIgnoreCase(tile.getDisease()))
                    .count();
            long totalTiles = tiles.size();

            BigDecimal diseasePct = BigDecimal.valueOf(diseasedTiles * 100.0 / totalTiles).setScale(2, RoundingMode.DOWN);
            diseasePct = diseasePct.setScale(2, RoundingMode.DOWN);

            return diseasePct + "%";
        }
    }

    @Override
    public String getTotalYieldByProjectId(Long id) {
        List<Tile> tiles = tileRepository.findAllByTaskProjectId(id);
        if (tiles.isEmpty()) {
            return "0.00";
        } else {
            BigDecimal totalYield = tiles.stream()
                    .map(tile -> new BigDecimal(tile.getYield()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalYield = totalYield.setScale(2, RoundingMode.DOWN);
            return totalYield.toString();
        }
    }

    @Override
    public String getTotalStressPctByProjectId(Long id) {
        List<Tile> tiles = tileRepository.findAllByTaskProjectId(id);
        if (tiles.isEmpty()) {
            return "0.00";
        } else {
            long stressedTiles = tiles.stream()
                    .filter(tile -> "yes".equalsIgnoreCase(tile.getStress()))
                    .count();
            long totalTiles = tiles.size();

            BigDecimal stressPct = BigDecimal.valueOf(stressedTiles * 100.0 / totalTiles).setScale(2, RoundingMode.DOWN);
            stressPct = stressPct.setScale(2, RoundingMode.DOWN);

            return stressPct.toString();
        }
    }

    @Override
    public String getTotalDiseasePctByProjectId(Long id) {
        List<Tile> tiles = tileRepository.findAllByTaskProjectId(id);
        if (tiles.isEmpty()) {
            return "0.00";
        } else {

            long diseasedTiles = tiles.stream()
                    .filter(tile -> "yes".equalsIgnoreCase(tile.getDisease()))
                    .count();
            long totalTiles = tiles.size();

            BigDecimal diseasePct = BigDecimal.valueOf(diseasedTiles * 100.0 / totalTiles).setScale(2, RoundingMode.DOWN);
            diseasePct = diseasePct.setScale(2, RoundingMode.DOWN);

            return diseasePct.toString();
        }
    }

    @Override
    public com.research.agrivision.business.entity.Task getRgbTaskByProjectId(Long id) {
        return taskRepository.findAllByProjectIdAndTaskType(id, TaskType.RGB).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Task::getLastModifiedDate).reversed())
                .map(task -> mapper.map(task, com.research.agrivision.business.entity.Task.class))
                .findFirst()
                .orElse(null);

    }

    @Override
    public com.research.agrivision.business.entity.Task getTaskByProjectIdAndType(Long id, TaskType type) {
        return taskRepository.findAllByProjectIdAndTaskType(id, type).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Task::getLastModifiedDate).reversed())
                .map(task -> mapper.map(task, com.research.agrivision.business.entity.Task.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateTask(com.research.agrivision.business.entity.Task task) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Task> optionalTask = taskRepository.findById(task.getId());
        if (optionalTask.isPresent()) {
            Task dbTask = optionalTask.get();
            dbTask.setMapImage(task.getMapImage());
            dbTask.setMapImageUrl(task.getMapImageUrl());
            dbTask.setMapImagePng(task.getMapImagePng());
            dbTask.setMapImagePngUrl(task.getMapImagePngUrl());
            dbTask.setLowerLat(task.getLowerLat());
            dbTask.setUpperLat(task.getUpperLat());
            dbTask.setLowerLng(task.getLowerLng());
            dbTask.setUpperLng(task.getUpperLng());
            taskRepository.save(dbTask);
        }
    }

    private void setPredictions(Tile tile) {
        DiseaseRequest stressRequest = new DiseaseRequest();
        ArrayList<Double> stressFeatures = new ArrayList<>();
        stressFeatures.add(tile.getRendvi());
        stressFeatures.add(tile.getTemperature());
        stressFeatures.add(tile.getHumidity());
        stressFeatures.add(tile.getUvLevel());
        stressFeatures.add(tile.getSoilMoisture());
        stressFeatures.add(tile.getPressure());
        stressFeatures.add(tile.getAltitude());

        stressRequest.setFeatures(stressFeatures);
        String stress = mlPort.getStressModel(stressRequest);
        tile.setStress(stress);

        DiseaseRequest diseaseRequest = new DiseaseRequest();
        ArrayList<Double> diseaseFeatures = new ArrayList<>();
        diseaseFeatures.add(tile.getNdvi());
        diseaseFeatures.add(tile.getRendvi());
        diseaseFeatures.add(tile.getCire());
        diseaseFeatures.add(tile.getPri());
        diseaseFeatures.add(tile.getTemperature());
        diseaseFeatures.add(tile.getHumidity());
        diseaseFeatures.add(tile.getUvLevel());

        diseaseRequest.setFeatures(diseaseFeatures);
        String disease = mlPort.getDiseaseModel(diseaseRequest);
        tile.setDisease(disease);

        DiseaseRequest yieldRequest = new DiseaseRequest();
        ArrayList<Double> yieldFeatures = new ArrayList<>();
        yieldFeatures.add(tile.getNdvi());
        yieldFeatures.add(tile.getTemperature());
        yieldFeatures.add(tile.getHumidity());
        yieldFeatures.add(tile.getUvLevel());
        yieldFeatures.add(tile.getSoilMoisture());
        yieldFeatures.add(tile.getPressure());
        yieldFeatures.add(tile.getAltitude());

        yieldRequest.setFeatures(yieldFeatures);
        String yield = mlPort.getYieldModel(yieldRequest);
        tile.setYield(yield);
    }
}
