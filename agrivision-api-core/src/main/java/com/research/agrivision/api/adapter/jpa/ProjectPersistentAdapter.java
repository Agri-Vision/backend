package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.jpa.entity.*;
import com.research.agrivision.api.adapter.jpa.repository.*;
import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.entity.imageTool.ToolReadings;
import com.research.agrivision.business.entity.ml.sample.DiseaseRequest;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.agrivision.business.enums.TaskType;
import com.research.agrivision.business.port.out.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        com.research.agrivision.api.adapter.jpa.entity.Project project = projectRepository.findById(request.getId()).orElse(null);
        com.research.agrivision.api.adapter.jpa.entity.Project dbProject = mapper.map(request, com.research.agrivision.api.adapter.jpa.entity.Project.class);
        if (dbProject.getTaskList() != null) {
            for (Task task : dbProject.getTaskList()) {
                task.setProject(project);
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
}
