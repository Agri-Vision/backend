package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Sample;

public interface SaveSamplePort {
    /**
     * Create sample
     * @param sample - sample object
     * @return sample
     */
    Sample createSample(Sample sample);

    /**
     * Update sample
     * @param sample -sample object
     * @return sample
     */
    Sample updateSample(Sample sample);

    /**
     * Delete sample by id
     * @param id - sample id
     */
    void deleteSampleByid(Long id);
}
