package com.research.agrivision.api.adapter.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "iot-api-client", url = "${iot.url}")
public interface IotClient {
    @GetMapping("/get_enviroment_data")
    String getEnvironmentData();
}
