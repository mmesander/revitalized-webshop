package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.specifications.ReviewSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.ReviewInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.repositories.ReviewRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    // Transfer Methods
    public static Review dtoToReview(ReviewInputDto inputDto) {
        Review review = new Review();

        copyProperties(inputDto, review);

        return review;
    }

    public static ReviewDto reviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();

        copyProperties(review, reviewDto);

        return reviewDto;
    }

    // CRUD Requests
    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewDto> reviewDtos = new ArrayList<>();

        for (Review review : reviews) {
            ReviewDto reviewDto = reviewToDto(review);
            reviewDtos.add(reviewDto);
        }

        if (reviewDtos.isEmpty()) {
            throw new RecordNotFoundException("No reviews found");
        } else {
            reviewDtos.sort(Comparator.comparing(ReviewDto::getId).reversed());

            return reviewDtos;
        }
    }

    public ReviewDto getReview(Long id) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isPresent()) {
            return reviewToDto(review.get());
        } else {
            throw new RecordNotFoundException("Review with id: " + id + " not found");
        }
    }

    public List<ReviewDto> getReviewsByParam(
            Integer rating,
            Integer minRating,
            Integer maxRating
    ) {
        Specification<Review> params = Specification.where
                (rating == null ? null : getReviewRatingLikeFilter(rating))
                .and(minRating == null ? null : getReviewMoreThanFilter(minRating))
                .and(maxRating == null ? null : getReviewLessThanFilter(maxRating));

        List<Review> filteredReviews = reviewRepository.findAll(params);
        List<ReviewDto> reviewDtos = new ArrayList<>();

        for (Review review : filteredReviews) {
            ReviewDto reviewDto = reviewToDto(review);
            reviewDtos.add(reviewDto);
        }

        if (reviewDtos.isEmpty()) {
            throw new RecordNotFoundException("No reviews found with the specified filters");
        } else {
            reviewDtos.sort(Comparator.comparing(ReviewDto::getId).reversed());

            return reviewDtos;
        }
    }

    public ReviewDto createReview(ReviewInputDto inputDto) {
        Review review = dtoToReview(inputDto);

        review.setDate(createDate());

        reviewRepository.save(review);

        return reviewToDto(review);
    }

    public ReviewDto updateReview(Long id, ReviewInputDto inputDto) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();

            copyProperties(inputDto, review);
            review.setDate(createDate());

            Review updatedReview = reviewRepository.save(review);

            return reviewToDto(updatedReview);
        } else {
            throw new RecordNotFoundException("No review found with id: " + id);
        }
    }

    public ReviewDto patchReview(Long id, ReviewInputDto inputDto) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();

            if (review.getReview() != null) {
                review.setReview(inputDto.getReview());
            }

            if (review.getRating() != null) {
                review.setRating(inputDto.getRating());
            }

            if (review.getReview() != null || review.getRating() != null) {
                review.setDate(createDate());
            }

            Review patchedReview = reviewRepository.save(review);

            return reviewToDto(patchedReview);
        } else {
            throw new RecordNotFoundException("No review found with id: " + id);
        }
    }


}
