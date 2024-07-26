package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.entity.Sample;
import com.research.hexa.core.UseCase;

import java.util.List;

@UseCase
public interface OrganizationUseCase {
    /**
     * Create organization
     * @param organization - organization object
     * @return organization
     */
    Organization createOrganization(Organization organization);

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
