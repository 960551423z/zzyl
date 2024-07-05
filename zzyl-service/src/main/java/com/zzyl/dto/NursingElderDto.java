package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class NursingElderDto extends BaseDto {
    private Long id;

    private List<Long> nursingIds;

    private Long elderId;
}