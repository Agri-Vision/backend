package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Sample;
import com.research.agrivision.business.port.in.SampleUseCase;
import com.research.agrivision.business.port.out.GetSamplePort;
import com.research.agrivision.business.port.out.SaveSamplePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleUseCaseImpl implements SampleUseCase {
    @Autowired
    private GetSamplePort getSamplePort;

    @Autowired
    private SaveSamplePort saveSamplePort;

    @Override
    public Sample createSample(Sample sample) {
        return saveSamplePort.createSample(sample);
    }

    @Override
    public Sample getSampleById(Long id) {
        return getSamplePort.getSampleById(id);
    }

    @Override
    public List<Sample> getAllSamples() {
        return getSamplePort.getAllSamples();
    }

    @Override
    public Sample updateSample(Sample sample) {
        return saveSamplePort.updateSample(sample);
    }

    @Override
    public void deleteSampleById(Long id) {
        saveSamplePort.deleteSampleByid(id);
    }
}
