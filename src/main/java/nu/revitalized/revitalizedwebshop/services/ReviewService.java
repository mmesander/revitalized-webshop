package nu.revitalized.revitalizedwebshop.services;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.helpers.FormatDate.formatDate;
import static nu.revitalized.revitalizedwebshop.helpers.BuildBadAssignRequest.buildBadAssignRequest;
import static nu.revitalized.revitalizedwebshop.helpers.CalculateAverageRating.calculateAverageRating;
import static nu.revitalized.revitalizedwebshop.services.SupplementService.*;
import static nu.revitalized.revitalizedwebshop.services.GarmentService.*;
import static nu.revitalized.revitalizedwebshop.specifications.ReviewSpecification.*;

import nu.revitalized.revitalizedwebshop.dtos.input.ReviewInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.repositories.ReviewRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
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

        if (review.getGarment() != null) {
            reviewDto.setProductId(review.getGarment().getId());
        }

        if (review.getSupplement() != null) {
            reviewDto.setProductId(review.getSupplement().getId());
        }

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
            reviewDtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());

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
            reviewDtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());

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

            if (inputDto.getReview() != null) {
                review.setReview(inputDto.getReview());
            }

            if (inputDto.getRating() != null) {
                review.setRating(inputDto.getRating());
            }

            if (inputDto.getReview() != null || inputDto.getRating() != null) {
                review.setDate(createDate());
            }

            Review patchedReview = reviewRepository.save(review);

            return reviewToDto(patchedReview);
        } else {
            throw new RecordNotFoundException("No review found with id: " + id);
        }
    }

    public String deleteReview(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            Set<Review> reviews;

            if (review.getSupplement() != null) {
                Supplement supplement = review.getSupplement();

                reviews = supplement.getReviews();
                reviews.remove(review);
                supplement.setReviews(reviews);
                supplement.setAverageRating(calculateAverageRating(supplement));
                supplementRepository.save(supplement);
                reviewRepository.deleteById(id);

                return "Review with id: " + id + " from: " + formatDate(optionalReview.get().getDate())
                        + " is removed from Supplement: " + supplement.getName() + " with id: " + supplement.getId();
            } else if (review.getGarment() != null) {
                Garment garment = review.getGarment();

                reviews = garment.getReviews();
                reviews.remove(review);
                garment.setReviews(reviews);
                garment.setAverageRating(calculateAverageRating(garment));
                garmentRepository.save(garment);
                reviewRepository.deleteById(id);

                return "Review with id: " + id + " from: " + formatDate(optionalReview.get().getDate())
                        + " is removed from Garment: " + garment.getName() + " with id: " + garment.getId();
            } else {
                reviewRepository.deleteById(id);

                return "Review with id: " + id + " from: " + formatDate(optionalReview.get().getDate()) + " is removed";
            }
        } else {
            throw new RecordNotFoundException("No review found with id: " + id);
        }
    }


    // Product Requests
    public Object assignReviewToProduct(Long productId, Long reviewId) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new BadRequestException("No review found with id:" + reviewId);
        }

        Review review = optionalReview.get();

        if (review.getSupplement() != null || review.getGarment() != null) {
            String type = review.getSupplement() != null ? "supplement" : "garment";
            String name = review.getSupplement() != null ? review.getSupplement().getName() : review.getGarment().getName();
            Long id = review.getSupplement() != null ? review.getSupplement().getId() : review.getGarment().getId();

            throw new BadRequestException(buildBadAssignRequest(type, name, id, reviewId));
        }

        Object objectDto;

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            review.setSupplement(supplement);
            supplement.getReviews().add(review);
            supplement.setAverageRating(calculateAverageRating(supplement));
            supplementRepository.save(supplement);
            objectDto = supplementToDto(supplement);
        } else if (optionalGarment.isPresent()) {
            Garment garment = optionalGarment.get();
            review.setGarment(garment);
            garment.getReviews().add(review);
            garment.setAverageRating(calculateAverageRating(garment));
            garmentRepository.save(garment);
            objectDto = garmentToDto(garment);
        } else {
            throw new BadRequestException("No product found with id: " + productId);
        }

        reviewRepository.save(review);
        return objectDto;
    }

    public Object removeReviewFromProduct(Long productId, Long reviewId) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Set<Review> reviews;

        if (optionalReview.isEmpty()) {
            throw new BadRequestException("No review found with id: " + reviewId);
        }

        Review review = optionalReview.get();
        Object objectDto;

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            reviews = supplement.getReviews();
            reviews.remove(review);
            reviewRepository.deleteById(reviewId);
            supplement.setReviews(reviews);
            supplement.setAverageRating(calculateAverageRating(supplement));
            supplementRepository.save(supplement);
            objectDto = supplementToDto(supplement);
        } else if (optionalGarment.isPresent()) {
            Garment garment = optionalGarment.get();
            reviews = garment.getReviews();
            reviews.remove(review);
            reviewRepository.deleteById(reviewId);
            garment.setReviews(reviews);
            garment.setAverageRating(calculateAverageRating(garment));
            garmentRepository.save(garment);
            objectDto = garmentToDto(garment);
        } else {
            throw new BadRequestException("No product found with id: " + productId);
        }

        return objectDto;
    }
}
