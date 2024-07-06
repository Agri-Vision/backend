package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.business.entity.Sample;
import com.research.agrivision.business.port.out.GetSamplePort;
import com.research.agrivision.business.port.out.SaveSamplePort;
import com.research.agrivision.api.adapter.jpa.repository.SampleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class SampleAdapter implements SaveSamplePort, GetSamplePort {
    private final SampleRepository sampleRepository;

    private ModelMapper mapper = new ModelMapper();

    public SampleAdapter(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Sample createSample(Sample sample) {
        if (sample == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.Sample dbSample = mapper.map(sample, com.research.agrivision.api.adapter.jpa.entity.Sample.class);
        sampleRepository.save(dbSample);
        return mapper.map(dbSample, Sample.class);
    }

    @Override
    public Sample updateSample(Sample sample) {
        if (sample == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.Sample dbSample = sampleRepository.save(mapper.map(sample, com.research.agrivision.api.adapter.jpa.entity.Sample.class));
        return mapper.map(dbSample, Sample.class);
    }

    @Override
    public void deleteSampleByid(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Sample> sample = sampleRepository.findById(id);
        if (sample.isPresent()) {
            sampleRepository.deleteById(id);
        }
    }

    @Override
    public Sample getSampleById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.Sample> sample = sampleRepository.findById(id);
        if (sample.isPresent()) {
            return mapper.map(sample, Sample.class);
        }
        return null;
    }

    @Override
    public List<Sample> getAllSamples() {
        return sampleRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.Sample::getLastModifiedDate).reversed())
                .map(sample -> mapper.map(sample, Sample.class))
                .toList();
    }
}
