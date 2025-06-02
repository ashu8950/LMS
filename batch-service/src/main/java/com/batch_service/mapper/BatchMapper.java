package com.batch_service.mapper;




import com.batch_service.dto.BatchDTO;
import com.batch_service.entity.Batch;

import org.springframework.stereotype.Component;

@Component
public class BatchMapper {

	public Batch toEntity(BatchDTO dto) {
	    return Batch.builder()
	            .id(dto.getId())
	            .name(dto.getName())
	            .description(dto.getDescription())
	            .courseId(dto.getCourseId())  // add this line
	            .build();
	}


    public BatchDTO toDTO(Batch entity) {
        return BatchDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .courseId(entity.getCourseId())
                .build();
    }
}
