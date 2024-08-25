package com.research.agrivision.api.adapter.integration;

import com.research.agrivision.api.adapter.clients.MlClient;
import com.research.agrivision.business.port.out.MlIntegrationPort;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MlIntegration implements MlIntegrationPort {
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private MlClient mlClient;
}
