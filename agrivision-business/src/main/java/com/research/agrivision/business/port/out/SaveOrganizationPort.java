package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Organization;

public interface SaveOrganizationPort {
    /**
     * Create organization
     * @param organization - organization object
     * @return organization
     */
    Organization createOrganization(Organization organization);

    /**
     * Update organization
     * @param organization - organization object
     * @return organization
     */
    Organization updateOrganization(Organization organization);

    /**
     * Delete organization by id
     * @param id - organization id
     */
    void deleteOrganizationById(Long id);
}
