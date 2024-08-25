package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Organization;

import java.util.List;

public interface GetOrganizationPort {
    /**
     * Get organization by id
     * @param id - organization id
     * @return organization
     */
    Organization getOrganizationById(Long id);

    /**
     * Get all organizations
     * @return list of organizations
     */
    List<Organization> getAllOrganizations();
}
