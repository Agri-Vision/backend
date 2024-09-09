package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.jpa.entity.*;
import com.research.agrivision.api.adapter.jpa.repository.*;
import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.port.out.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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
}
