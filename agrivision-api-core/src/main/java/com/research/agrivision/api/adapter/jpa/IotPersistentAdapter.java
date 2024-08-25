package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.jpa.repository.IotReadingRepository;
import com.research.agrivision.business.entity.IotReading;
import com.research.agrivision.business.port.out.GetIotPort;
import com.research.agrivision.business.port.out.SaveIotPort;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IotPersistentAdapter implements SaveIotPort, GetIotPort {
    private final IotReadingRepository iotReadingRepository;

    private ModelMapper mapper = new ModelMapper();

    public IotPersistentAdapter(IotReadingRepository iotReadingRepository) {
        this.iotReadingRepository = iotReadingRepository;
    }

    @Override
    public IotReading createIotReading(IotReading iotReading) {
        if (iotReading == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.IotReading dbIotReading = mapper.map(iotReading, com.research.agrivision.api.adapter.jpa.entity.IotReading.class);
        iotReadingRepository.save(dbIotReading);
        return mapper.map(dbIotReading, IotReading.class);
    }

    @Override
    public IotReading getIotReadingById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.IotReading> iotReading = iotReadingRepository.findById(id);
        if (iotReading.isPresent()) {
            return mapper.map(iotReading, com.research.agrivision.business.entity.IotReading.class);
        }
        return null;
    }

    @Override
    public List<IotReading> getAllIotReadings() {
        return iotReadingRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.IotReading::getLastModifiedDate).reversed())
                .map(iotReading -> mapper.map(iotReading, com.research.agrivision.business.entity.IotReading.class))
                .toList();
    }

    @Override
    public List<IotReading> getIotReadingByTimePeriod(String time) {
        LocalDate today = LocalDate.now();
//        LocalDate today = LocalDate.of(2024, 7, 24);
        String timeLower = time.toLowerCase();

        LocalDate date = null;
        LocalDate startOfWeek = null;
        LocalDate endOfWeek = null;
        LocalDate startOfMonth = null;
        LocalDate endOfMonth = null;

        switch (timeLower) {
            case "daily":
                date = today;
                break;
            case "weekly":
                startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            case "monthly":
                startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
                endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
                break;
            default:
                throw new IllegalArgumentException("Invalid time parameter: " + time);
        }

        return iotReadingRepository.findByTime(
                        timeLower,
                        date,
                        startOfWeek,
                        endOfWeek,
                        startOfMonth,
                        endOfMonth
                ).stream()
                .map(iotReadingEntity -> mapper.map(iotReadingEntity, IotReading.class))
                .collect(Collectors.toList());
    }
}
