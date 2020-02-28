package com.youblog.review.mappers;

import java.util.List;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.youblog.review.persistence.model.Review;
import com.youblog.review.service.dto.ReviewDTO;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

	@Mappings({ @Mapping(source = "id", target = "reviewId", qualifiedByName = "hexStrToObjectId") })
	ReviewDTO entityToApi(Review entity);

	@Named("objectIdToHexStr")
	public static String objectIdToHexStr(ObjectId id) {
		if(id==null) return null;
		return id.toHexString();
	}
	
	@Named("hexStrToObjectId")
	public static ObjectId hexStrToObjectId(String id) {
		if(id==null) return null;
		return new ObjectId(id);
	}

	@Mappings({ @Mapping(target = "id", source = "reviewId", qualifiedByName = "objectIdToHexStr"),
			@Mapping(target = "version", ignore = true) })
	Review apiToEntity(ReviewDTO api);

	List<ReviewDTO> entityListToApiList(List<Review> entity);

	List<Review> apiListToEntityList(List<ReviewDTO> api);
}
