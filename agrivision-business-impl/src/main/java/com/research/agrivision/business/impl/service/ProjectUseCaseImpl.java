package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.*;
import com.research.agrivision.business.entity.imageTool.ToolReadings;
import com.research.agrivision.business.entity.project.ProjectHistory;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.agrivision.business.enums.TaskType;
import com.research.agrivision.business.port.in.ProjectUseCase;
import com.research.agrivision.business.port.out.*;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.List;

import org.geotools.geometry.DirectPosition2D;
import org.springframework.mock.web.MockMultipartFile;

@Service
public class ProjectUseCaseImpl implements ProjectUseCase {
    @Autowired
    private SaveProjectPort saveProjectPort;

    @Autowired
    private GetProjectPort getProjectPort;

    @Autowired
    private SaveIotPort saveIotPort;

    @Autowired
    private GetIotPort getIotPort;

    @Autowired
    private SaveTaskPort saveTaskPort;

    @Autowired
    private GetTaskPort getTaskPort;

    @Autowired
    private SaveTilePort saveTilePort;

    @Autowired
    private GetTilePort getTilePort;

    @Autowired
    private FilePort filePort;

    @Autowired
    private WebOdmPort webOdmPort;

    @Override
    public Project createProject(Project project) {
        project.setStatus(ProjectStatus.NEW);
        if (project.getTaskList() != null && !project.getTaskList().isEmpty()) {
            for (Task task : project.getTaskList()) {
                generateTaskSignedUrl(task);
            }
        }
        return saveProjectPort.createProject(project);
    }

    @Override
    public Project getProjectById(Long id) {
        return getProjectPort.getProjectById(id);
    }

    @Override
    public Project updateProject(Project request) {
        request.setStatus(ProjectStatus.PENDING);
        if (request.getTaskList() != null && !request.getTaskList().isEmpty()) {
            for (Task task : request.getTaskList()) {
                generateTaskSignedUrl(task);
//            if (project.getWebOdmProjectId() != null && task.getWebOdmTaskId() != null && !task.isStatus()) {
//                webOdmPort.getWebOdmTask(project.getWebOdmProjectId(), task.getWebOdmTaskId());
//            }
            }
        }
        return saveProjectPort.updateProject(request);
    }

    @Override
    public List<Project> getAllProjects() {
        return getProjectPort.getAllProjects();
    }

    @Override
    public List<Project> getAllProjectsByPlantationId(Long id) {
        return getProjectPort.getAllProjectsByPlantationId(id);
    }

    @Override
    public Project getProjectByWebOdmProjectId(String projectId) {
        return getProjectPort.getProjectByWebOdmProjectId(projectId);
    }

    @Override
    public Task getTaskByWebOdmTaskId(String taskId) {
        return getTaskPort.getTaskByWebOdmTaskId(taskId);
    }

    @Override
    public void createTile(ToolReadings toolReadings) {
        saveTilePort.createTile(toolReadings);
    }

    @Override
    public List<Project> getAllProjectsByStatus(ProjectStatus status) {
        return getProjectPort.getAllProjectsByStatus(status);
    }

    @Override
    public Tile getTileById(Long tileId) {
        return getTilePort.getTileById(tileId);
    }

    @Override
    public List<Tile> getAllTiles() {
        return getTilePort.getAllTiles();
    }

    @Override
    public Task getRgbTaskByProjectId(Long id) {
        return getTaskPort.getRgbTaskByProjectId(id);
    }

    @Override
    public Task getTaskById(Long id) {
        return getTaskPort.getTaskById(id);
    }

    @Override
    public void updateTask(Task task) {
        saveTaskPort.updateTask(task);
    }

    @Override
    @Async
    public void updateProjectMaps(Long id, MultipartFile rgbMap) {
        Task rgbTask = getTaskPort.getTaskByProjectIdAndType(id, TaskType.RGB);
        if (rgbTask != null) {
            try {
                String tifFileName = uploadTiffFile(rgbMap);
                rgbTask.setMapImage(tifFileName);

                String pngFileName = convertTiffToPng(rgbMap);
                rgbTask.setMapImagePng(pngFileName);

                extractGeoCoordinates(rgbMap, rgbTask);

                generateTaskSignedUrl(rgbTask);
                saveTaskPort.updateTask(rgbTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Project> getAllProjectsByAgent(Long id) {
        return getProjectPort.getAllProjectsByAgent(id);
    }

    @Override
    public void createTilesByTaskId(Long id, List<Tile> tileList) {
        saveTilePort.createTilesByTaskId(id, tileList);
    }

    @Override
    public List<Tile> getAllTilesByTaskId(Long id) {
        return getTilePort.getAllTilesByTaskId(id);
    }

    @Override
    public List<Tile> getAllTilesByProjectId(Long id) {
        return getTilePort.getAllTilesByProjectId(id);
    }

    @Override
    public List<ProjectHistory> getProjectHistoryByPlantationId(Long id) {
        return getProjectPort.getProjectHistoryByPlantationId(id);
    }

    @Override
    public long getProjectCountByPlantationId(Long id) {
        return getProjectPort.getProjectCountByPlantationId(id);
    }

    @Override
    public String getTotalYield() {
        return getTilePort.getTotalYield();
    }

    private void generateTaskSignedUrl(Task task) {
        if(task.getMapImage() != null) {
            String imgName = task.getMapImage();
            task.setMapImageUrl(filePort.generateSignedUrl(imgName));
        }
        if(task.getMapImagePng() != null) {
            String pngName = task.getMapImagePng();
            task.setMapImagePngUrl(filePort.generateSignedUrl(pngName));
        }
    }

//    private void generateTileSignedUrl(Tile tile) {
//        if(tile !=null && tile.getTileImage() != null) {
//            String imgName = tile.getTileImage();
//            tile.setTileImageUrl(filePort.generateSignedUrl(imgName));
//        }
//    }

    private void generatePlantationSignedUrl(Plantation plantation) {
        if(plantation !=null && plantation.getPlantationImg() != null) {
            String imgName = plantation.getPlantationImg();
            plantation.setPlantationImgUrl(filePort.generateSignedUrl(imgName));
        }
    }

    private void generateAgentSignedUrl(User agent) {
        if(agent !=null && agent.getProfileImg() != null) {
            String imgName = agent.getProfileImg();
            agent.setProfileImgUrl(filePort.generateSignedUrl(imgName));
        }
    }

    private String uploadTiffFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            String fileName = System.currentTimeMillis() + "." + extension;
            byte[] fileBytes = file.getBytes();
            String base64Data = Base64.getEncoder().encodeToString(fileBytes);

            return filePort.uploadFile(base64Data, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertTiffToPng(MultipartFile tifFile) {
        try {
            BufferedImage tifImage = ImageIO.read(tifFile.getInputStream());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(tifImage, "png", baos);

            byte[] pngBytes = baos.toByteArray();

            MultipartFile pngMultipartFile = new MockMultipartFile(
                    "converted_image",
                    "converted_image.png",
                    "image/png",
                    pngBytes
            );

            return uploadTiffFile(pngMultipartFile);
        } catch (IOException e) {
            throw new RuntimeException("Error converting .tif to .png", e);
        }
    }

    private void extractGeoCoordinates(MultipartFile tifFile, Task task) {
        try {
            File file = convertMultipartFileToFile(tifFile);

            AbstractGridCoverage2DReader reader = new GeoTiffReader(file);
            GridCoverage2D coverage = reader.read(null);

            Envelope2D envelope = coverage.getEnvelope2D();

            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();

            CoordinateReferenceSystem targetCRS = DefaultGeographicCRS.WGS84; // geographic CRS (EPSG:4326)
            MathTransform transform = CRS.findMathTransform(crs, targetCRS, true);

            // create DirectPosition objects for lower and upper corners
            DirectPosition lowerCorner = new DirectPosition2D(envelope.getMinimum(0), envelope.getMinimum(1));
            DirectPosition upperCorner = new DirectPosition2D(envelope.getMaximum(0), envelope.getMaximum(1));

            DirectPosition transformedLower = transform.transform(lowerCorner, null);
            DirectPosition transformedUpper = transform.transform(upperCorner, null);

            // extract the geographic bounds
            double lowerLng = transformedLower.getOrdinate(0);
            double lowerLat = transformedLower.getOrdinate(1);
            double upperLng = transformedUpper.getOrdinate(0);
            double upperLat = transformedUpper.getOrdinate(1);

            task.setUpperLat(String.valueOf(upperLat));
            task.setLowerLat(String.valueOf(lowerLat));
            task.setUpperLng(String.valueOf(upperLng));
            task.setLowerLng(String.valueOf(lowerLng));
        } catch (IOException | TransformException e) {
            throw new RuntimeException("Error reading GeoTIFF file", e);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
        return convFile;
    }
}
