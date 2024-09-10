package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.entity.Plantation;
import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.port.in.OrganizationUseCase;
import com.research.agrivision.business.port.out.FilePort;
import com.research.agrivision.business.port.out.GetOrganizationPort;
import com.research.agrivision.business.port.out.GetPlantationPort;
import com.research.agrivision.business.port.out.SaveOrganizationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationUseCaseImpl implements OrganizationUseCase {
    @Autowired
    private GetOrganizationPort getOrganizationPort;

    @Autowired
    private SaveOrganizationPort saveOrganizationPort;

    @Autowired
    private GetPlantationPort getPlantationPort;

    @Autowired
    private FilePort filePort;

    @Override
    public Organization createOrganization(Organization organization) {
        Organization dbOrganization = saveOrganizationPort.createOrganization(organization);
        generateOrganizationSignedUrl(dbOrganization);
        if (dbOrganization.getPlantationList() != null && !dbOrganization.getPlantationList().isEmpty()) {
            for (Plantation plantation : dbOrganization.getPlantationList()) {
                generatePlantationSignedUrl(plantation);
            }
        }
        return dbOrganization;
    }

    @Override
    public Organization getOrganizationById(Long id) {
        Organization organization = getOrganizationPort.getOrganizationById(id);
        if (organization == null) return null;
        generateOrganizationSignedUrl(organization);
        if (organization.getPlantationList() != null && !organization.getPlantationList().isEmpty()) {
            for (Plantation plantation : organization.getPlantationList()) {
                generatePlantationSignedUrl(plantation);
            }
        }
        return organization;
    }

    @Override
    public List<Organization> getAllOrganizations() {
        List<Organization> organizationList = getOrganizationPort.getAllOrganizations();
        if (organizationList == null || organizationList.isEmpty()) return organizationList;
        for (Organization organization : organizationList) {
            generateOrganizationSignedUrl(organization);

            if (organization.getPlantationList() != null && !organization.getPlantationList().isEmpty()) {
                for (Plantation plantation : organization.getPlantationList()) {
                    generatePlantationSignedUrl(plantation);
                }
            }
        }
        return organizationList;
    }

    @Override
    public Organization updateOrganization(Organization organization) {
        Organization dbOrganization = saveOrganizationPort.updateOrganization(organization);
        generateOrganizationSignedUrl(dbOrganization);
        if (dbOrganization.getPlantationList() != null && !dbOrganization.getPlantationList().isEmpty()) {
            for (Plantation plantation : dbOrganization.getPlantationList()) {
                generatePlantationSignedUrl(plantation);
            }
        }
        return dbOrganization;
    }

    @Override
    public void deleteOrganizationById(Long id) {
        saveOrganizationPort.deleteOrganizationById(id);
    }

    @Override
    public Plantation getPlantationById(Long plantationId) {
        return getPlantationPort.getPlantationById(plantationId);
    }

    private void generateOrganizationSignedUrl(Organization organization) {
        if(organization.getOrgImage() != null) {
            String imgName = organization.getOrgImage();
            organization.setOrgImageUrl(filePort.generateSignedUrl(imgName));
        }
    }

    private void generatePlantationSignedUrl(Plantation plantation) {
        if(plantation.getPlantationImg() != null) {
            String imgName = plantation.getPlantationImg();
            plantation.setPlantationImgUrl(filePort.generateSignedUrl(imgName));
        }
    }
}
