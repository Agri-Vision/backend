package com.research.agrivision.api;

import com.research.agrivision.business.impl.service.IotUseCaseImpl;
import com.research.agrivision.business.impl.service.MlUseCaseImpl;
import com.research.agrivision.business.impl.service.OrganizationUseCaseImpl;
import com.research.agrivision.business.impl.service.SampleUseCaseImpl;
import com.research.agrivision.business.port.in.IotUseCase;
import com.research.agrivision.business.port.in.MlUseCase;
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

    @Bean
    public IotUseCase iotUseCase() {
        return new IotUseCaseImpl();
    }

    @Bean
    MlUseCase mlUseCase() {
        return new MlUseCaseImpl();
    }
}
