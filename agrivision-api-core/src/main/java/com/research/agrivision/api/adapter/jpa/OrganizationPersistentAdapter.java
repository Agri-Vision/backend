package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.jpa.entity.Plantation;
import com.research.agrivision.api.adapter.jpa.repository.OrganizationRepository;
import com.research.agrivision.api.adapter.jpa.repository.PlantationRepository;
import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.port.out.GetOrganizationPort;
import com.research.agrivision.business.port.out.GetPlantationPort;
import com.research.agrivision.business.port.out.SaveOrganizationPort;
import com.research.agrivision.business.port.out.SavePlantationPort;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class OrganizationPersistentAdapter implements SaveOrganizationPort, GetOrganizationPort, SavePlantationPort, GetPlantationPort {
    private final OrganizationRepository organizationRepository;
    private final PlantationRepository plantationRepository;

    private ModelMapper mapper = new ModelMapper();

    public OrganizationPersistentAdapter(OrganizationRepository organizationRepository, PlantationRepository plantationRepository) {
        this.organizationRepository = organizationRepository;
        this.plantationRepository = plantationRepository;
    }

    @Override
    public Organization getOrganizationById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Organization> organization = organizationRepository.findById(id);
        if (organization.isPresent()) {
            return mapper.map(organization, com.research.agrivision.business.entity.Organization.class);
        }
        return null;
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Organization::getLastModifiedDate).reversed())
                .map(organization -> mapper.map(organization, com.research.agrivision.business.entity.Organization.class))
                .toList();
    }

    @Override
    public Organization createOrganization(Organization organization) {
        if (organization == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.Organization dbOrganization = mapper.map(organization, com.research.agrivision.api.adapter.jpa.entity.Organization.class);
        organizationRepository.save(dbOrganization);
        return mapper.map(dbOrganization, Organization.class);
    }

    @Override
    public Organization updateOrganization(Organization organization) {
        if (organization == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.Organization dbOrganization = mapper.map(organization, com.research.agrivision.api.adapter.jpa.entity.Organization.class);
        if (dbOrganization.getPlantationList() != null) {
            for (Plantation plantation : dbOrganization.getPlantationList()) {
                plantation.setOrganization(dbOrganization);
            }
        }
        organizationRepository.save(dbOrganization);
        return mapper.map(dbOrganization, Organization.class);
    }

    @Override
    public void deleteOrganizationById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Organization> organization = organizationRepository.findById(id);
        if (organization.isPresent()) {
            organizationRepository.deleteById(id);
        }
    }

    @Override
    public com.research.agrivision.business.entity.Plantation getPlantationById(Long plantationId) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Plantation> plantation = plantationRepository.findById(plantationId);
        if (plantation.isPresent()) {
            return mapper.map(plantation, com.research.agrivision.business.entity.Plantation.class);
        }
        return null;
    }

    @Override
    public List<com.research.agrivision.business.entity.Plantation> getAllPlantations() {
        return plantationRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Plantation::getLastModifiedDate).reversed())
                .map(plantation -> mapper.map(plantation, com.research.agrivision.business.entity.Plantation.class))
                .toList();
    }
}
