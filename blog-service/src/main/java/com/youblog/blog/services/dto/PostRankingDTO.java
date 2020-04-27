package com.youblog.blog.services.dto;

public class PostRankingDTO extends PostDTO {
	private Float ranking;
	private BlogUserDetails user;

	private PostRankingDTO(PostRankingDTOBuilder builder) {
		super(builder);
		this.setRanking(builder.ranking);
		this.setUser(builder.user);
	}
	
	public static class PostRankingDTOBuilder extends PostDTO.PostDTOBuilder<PostRankingDTOBuilder>{
		private Float ranking;
		private BlogUserDetails user;

		public static PostRankingDTOBuilder withDtoBuilder() {
			return new PostRankingDTOBuilder();
		}
		
		public static PostRankingDTOBuilder withPostDto(PostDTO post) {
			return new PostRankingDTOBuilder()
					.id(post.getId())
					.port(post.getPort())
					.title(post.getTitle())
					.summary(post.getSummary())
					.content(post.getContent())
					.datePosted(post.getDatePosted())
					.dateUpdated(post.getDateUpdated())
					.blogUserId(post.getBlogUserId());
		}

		private PostRankingDTOBuilder() {

		}
		
		public  PostRankingDTOBuilder ranking(Float ranking) {
			this.ranking=ranking;
			return this;
		}
		
		public  PostRankingDTOBuilder user(BlogUserDetails user) {
			this.user=user;
			return this;
		}
		
		@Override
		public PostRankingDTO build() 
        { 
            return new PostRankingDTO(this); 
        } 
	}

	public Float getRanking() {
		return ranking;
	}

	public void setRanking(Float ranking) {
		this.ranking = ranking;
	}

	public BlogUserDetails getUser() {
		return user;
	}

	public void setUser(BlogUserDetails user) {
		this.user = user;
	}
}
