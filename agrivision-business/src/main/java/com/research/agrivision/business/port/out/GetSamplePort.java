package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Sample;

import java.util.List;

public interface GetSamplePort {
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
}
