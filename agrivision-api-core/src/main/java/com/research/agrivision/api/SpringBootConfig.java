package com.research.agrivision.api;

import com.research.agrivision.business.impl.service.*;
import com.research.agrivision.business.port.in.*;
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

    @Bean
    WebOdmUseCase webOdmUseCase() {
        return new WebOdmUseCaseImpl();
    }

    @Bean
    FileUseCase fileUseCase() {
        return new FileUseCaseImpl();
    }

    @Bean
    ProjectUseCase projectUseCase() {
        return new ProjectUseCaseImpl();
    }

    @Bean
    UserManagementUseCase userManagementUseCase() {
        return new UserManagementUseCaseImpl();
    }

    @Bean
    DiseaseUseCase diseaseUseCase() {
        return new DiseaseUseCaseImpl();
    }

    @Bean
    StressUseCase stressUseCase() {
        return new StressUseCaseImpl();
    }

    @Bean
    YieldUseCase yieldUseCase() {
        return new YieldUseCaseImpl();
    }

    @Bean
    PlantationUseCase plantationUseCase() {
        return new PlantationUseCaseImpl();
    }
}
