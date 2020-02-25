package com.youblog.review.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.youblog.review.persistence.model.Review;
import com.youblog.review.service.dto.ReviewDTO;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

	ReviewDTO entityToApi(Review entity);

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true) })
	Review apiToEntity(ReviewDTO api);

	List<ReviewDTO> entityListToApiList(List<Review> entity);

	List<Review> apiListToEntityList(List<ReviewDTO> api);
}
