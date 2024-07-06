package com.research.agrivision.api.adapter.api.mapper;

import com.research.agrivision.business.entity.Sample;
import com.research.agrivision.api.adapter.api.request.SampleRequest;
import com.research.agrivision.api.adapter.api.response.SampleResponse;
import com.research.hexa.core.ModelMapper;

import java.util.List;

public class SampleMapper implements ModelMapper<Sample, SampleRequest, SampleResponse> {
    private org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();

    @Override
    public Sample requestToEntity(SampleRequest request) {
        if (request == null) {
            return null;
        }
        return mapper.map(request, Sample.class);
    }

    @Override
    public SampleResponse entityToResponse(Sample entity) {
        if (entity == null) {
            return null;
        }
        return mapper.map(entity, SampleResponse.class);
    }

    @Override
    public List<SampleResponse> entityToResponse(List<Sample> entities) {
        return entities.stream()
                .map(this::entityToResponse)
                .toList();
    }
}
