package com.research.agrivision.api.adapter.api.mapper;

import com.research.agrivision.api.adapter.api.request.OrganizationRequest;
import com.research.agrivision.api.adapter.api.response.OrganizationResponse;
import com.research.agrivision.business.entity.Organization;
import com.research.hexa.core.ModelMapper;

import java.util.List;

public class OrganizationMapper implements ModelMapper<Organization, OrganizationRequest, OrganizationResponse> {
    private org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();

    @Override
    public Organization requestToEntity(OrganizationRequest request) {
        if (request == null) {
            return null;
        }
        return mapper.map(request, Organization.class);
    }

    @Override
    public OrganizationResponse entityToResponse(Organization entity) {
        if (entity == null) {
            return null;
        }
        return mapper.map(entity, OrganizationResponse.class);
    }

    @Override
    public List<OrganizationResponse> entityToResponse(List<Organization> entities) {
        return entities.stream()
                .map(this::entityToResponse)
                .toList();
    }
}
