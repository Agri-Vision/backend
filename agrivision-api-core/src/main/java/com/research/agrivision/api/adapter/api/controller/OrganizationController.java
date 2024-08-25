package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.mapper.OrganizationMapper;
import com.research.agrivision.api.adapter.api.request.OrganizationRequest;
import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.api.adapter.api.response.OrganizationResponse;
import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.port.in.OrganizationUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/org")
public class OrganizationController {
    private final OrganizationUseCase organizationService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final com.research.hexa.core.ModelMapper<Organization, OrganizationRequest, OrganizationResponse> mapper = new OrganizationMapper();

    public OrganizationController(OrganizationUseCase organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping()
    public ResponseEntity<OrganizationResponse> createOrganization(@RequestBody final OrganizationRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Organization organization = organizationService.createOrganization(mapper.requestToEntity(request));
        return ResponseEntity.ok(mapper.entityToResponse(organization));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
        Organization organization = organizationService.getOrganizationById(id);
        if (organization == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(mapper.entityToResponse(organization));
    }

    @GetMapping()
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
        List<Organization> organizationList = organizationService.getAllOrganizations();
        return ResponseEntity.ok(mapper.entityToResponse(organizationList));
    }

    @PutMapping()
    public ResponseEntity<OrganizationResponse> updateOrganization(@RequestBody final OrganizationRequest request) {
        if (request == null || request.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Organization organization = organizationService.getOrganizationById(request.getId());

        if (organization == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Organization updatedOrganization = organizationService.updateOrganization(mapper.requestToEntity(request));
        return ResponseEntity.ok(mapper.entityToResponse(updatedOrganization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteOrganizationById(@PathVariable Long id) {
        Organization organization = organizationService.getOrganizationById(id);

        if (organization == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        organizationService.deleteOrganizationById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("Successfully deleted organization with id: " + id));
    }
}
