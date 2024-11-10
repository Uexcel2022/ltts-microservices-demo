package com.uexcel.bus.dto;

import com.uexcel.bus.entity.Bus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class BusResponseDto {
    private List<Bus> bus;
}
