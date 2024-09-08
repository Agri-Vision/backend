package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.port.in.OrganizationUseCase;
import com.research.agrivision.business.port.out.FilePort;
import com.research.agrivision.business.port.out.GetOrganizationPort;
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
    private FilePort filePort;

    @Override
    public Organization createOrganization(Organization organization) {
        return saveOrganizationPort.createOrganization(organization);
    }

    @Override
    public Organization getOrganizationById(Long id) {
        Organization organization = getOrganizationPort.getOrganizationById(id);
        if (organization == null) return null;
        String fileName = organization.getOrgImage();
        organization.setOrgImageUrl(filePort.generateSignedUrl(fileName));
        return organization;
    }

    @Override
    public List<Organization> getAllOrganizations() {
        List<Organization> organizationList = getOrganizationPort.getAllOrganizations();
        if (organizationList == null || organizationList.isEmpty()) return organizationList;
        for (Organization organization : organizationList) {
            String fileName = organization.getOrgImage();
            organization.setOrgImageUrl(filePort.generateSignedUrl(fileName));
        }
        return organizationList;
    }

    @Override
    public Organization updateOrganization(Organization organization) {
        return saveOrganizationPort.updateOrganization(organization);
    }

    @Override
    public void deleteOrganizationById(Long id) {
        saveOrganizationPort.deleteOrganizationById(id);
    }
}
