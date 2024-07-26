package com.research.agrivision.api;

import com.research.agrivision.business.impl.service.OrganizationUseCaseImpl;
import com.research.agrivision.business.impl.service.SampleUseCaseImpl;
import com.research.agrivision.business.port.in.OrganizationUseCase;
import com.research.agrivision.business.port.in.SampleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootConfig {

    @Bean
    public SampleUseCase sampleUseCase() {
        return new SampleUseCaseImpl();
    }

    @Bean
    public OrganizationUseCase organizationUseCase() {
        return new OrganizationUseCaseImpl();
    }
}
