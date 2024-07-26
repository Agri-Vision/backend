package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.port.in.OrganizationUseCase;
import com.research.agrivision.business.port.out.GetOrganizationPort;
import com.research.agrivision.business.port.out.SaveOrganizationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationUseCaseImpl implements OrganizationUseCase {
    @Autowired
    private GetOrganizationPort getOrganizationPort;

    @Autowired
    private SaveOrganizationPort saveOrganizationPort;

    @Override
    public Organization createOrganization(Organization organization) {
        return saveOrganizationPort.createOrganization(organization);
    }

    @Override
    public Organization getOrganizationById(Long id) {
        return getOrganizationPort.getOrganizationById(id);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return getOrganizationPort.getAllOrganizations();
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
