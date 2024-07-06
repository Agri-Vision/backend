package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Sample;
import com.research.hexa.core.UseCase;

import java.util.List;

@UseCase
public interface SampleUseCase {
    /**
     * Create sample
     * @param sample - sample object
     * @return sample
     */
    Sample createSample(Sample sample);

    /**
     * Get sample by id
     * @param id - sample id
     * @return sample
     */
    Sample getSampleById(Long id);

    /**
     * Get all samples
     * @return list of samples
     */
    List<Sample> getAllSamples();

    /**
     * Update sample
     * @param sample - sample object
     * @return sample
     */
    Sample updateSample(Sample sample);

    /**
     * Delete sample by id
     * @param id - sample id
     */
    void deleteSampleById(Long id);
}
