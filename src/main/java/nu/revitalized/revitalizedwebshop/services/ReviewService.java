package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.helpers.FormatDate.formatDate;
import static nu.revitalized.revitalizedwebshop.helpers.UpdateRating.*;
import static nu.revitalized.revitalizedwebshop.services.SupplementService.*;
import static nu.revitalized.revitalizedwebshop.services.GarmentService.*;
import static nu.revitalized.revitalizedwebshop.specifications.ReviewSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.ReviewInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.repositories.ReviewRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
        this.userRepository = userRepository;
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

        if (review.getUser() != null) {
            reviewDto.setUsername(review.getUser().getUsername());
        }

        return reviewDto;
    }

    // CRUD Methods
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

            if (updatedReview.getSupplement() != null) {
                supplementRepository.save(updateSupplementRating(
                        updatedReview, updatedReview.getSupplement(), false, true
                ));
            }

            if (updatedReview.getGarment() != null) {
                garmentRepository.save(updateGarmentRating(
                        updatedReview, updatedReview.getGarment(), false, true
                ));
            }

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

                if (review.getSupplement() != null) {
                    supplementRepository.save(updateSupplementRating(
                            review, review.getSupplement(), false, true
                    ));
                }

                if (review.getGarment() != null) {
                    garmentRepository.save(updateGarmentRating(
                            review, review.getGarment(), false, true
                    ));
                }
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

            if (review.getSupplement() != null) {
                supplementRepository.save(updateSupplementRating(review, review.getSupplement(), true, false));
                reviewRepository.deleteById(id);

                return "Review with id: " + id + " from: " + formatDate(optionalReview.get().getDate())
                        + " is removed from Supplement: " + review.getSupplement().getName() + " with id: "
                        + review.getSupplement().getId();
            } else if (review.getGarment() != null) {
                garmentRepository.save(updateGarmentRating(review, review.getGarment(), true, false));
                reviewRepository.deleteById(id);

                return "Review with id: " + id + " from: " + formatDate(optionalReview.get().getDate())
                        + " is removed from Garment: " + review.getGarment().getName() + " with id: "
                        + review.getGarment().getId();
            } else {
                reviewRepository.deleteById(id);

                return "Review with id: " + id + " from: " + formatDate(optionalReview.get().getDate()) + " is removed";
            }
        } else {
            throw new RecordNotFoundException("No review found with id: " + id);
        }
    }

    // Relation - Product Methods
    public Object assignReviewToProduct(Long productId, Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalReview.isEmpty()) {
            throw new BadRequestException("No review found with id:" + reviewId);
        }

        Review review = optionalReview.get();

        if (review.getSupplement() != null || review.getGarment() != null) {
            String type = review.getSupplement() != null ? "supplement" : "garment";
            String name = review.getSupplement() != null ? review.getSupplement().getName() : review.getGarment().getName();
            Long id = review.getSupplement() != null ? review.getSupplement().getId() : review.getGarment().getId();

            throw new BadRequestException("Review with id: " + reviewId + " is already assigned to " + type
                    + ": " + name + " with id: " + id);
        }

        Object objectDto;

        if (optionalSupplement.isPresent()) {
            Supplement updatedSupplement = supplementRepository.save(
                    updateSupplementRating(review, optionalSupplement.get(), false, false));

            objectDto = supplementToDto(updatedSupplement);
        } else if (optionalGarment.isPresent()) {
            Garment updatedGarment = garmentRepository.save(
                    updateGarmentRating(review, optionalGarment.get(), false, false));

            objectDto = garmentToDto(updatedGarment);
        } else {
            throw new BadRequestException("No product found with id: " + productId);
        }

        reviewRepository.save(review);
        return objectDto;
    }

    public Object removeReviewFromProduct(Long productId, Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalReview.isEmpty()) {
            throw new BadRequestException("No review found with id: " + reviewId);
        }

        Review review = optionalReview.get();
        Object objectDto;

        if (optionalSupplement.isPresent()) {
            Supplement updatedSupplement = supplementRepository.save(
                    updateSupplementRating(review, review.getSupplement(), true, false));

            reviewRepository.deleteById(reviewId);

            objectDto = supplementToDto(updatedSupplement);
        } else if (optionalGarment.isPresent()) {
            Garment updatedGarment = garmentRepository.save(
                    updateGarmentRating(review, review.getGarment(), true, false));

            reviewRepository.deleteById(reviewId);

            objectDto = garmentToDto(updatedGarment);
        } else {
            throw new BadRequestException("No product found with id: " + productId);
        }

        return objectDto;
    }

    // Relation - Authenticated User Methods
    public List<ReviewDto> getAllPersonalReviews(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        Set<Review> reviews;
        List<ReviewDto> reviewDtos = new ArrayList<>();

        if (optionalUser.isPresent()) {
            reviews = optionalUser.get().getReviews();
        } else {
            throw new UsernameNotFoundException(username);
        }

        for (Review review : reviews) {
            ReviewDto reviewDto = reviewToDto(review);
            reviewDtos.add(reviewDto);
        }

        if (reviewDtos.isEmpty()) {
            throw new RecordNotFoundException("No reviews found from user: " + username);
        } else {
            reviewDtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());

            return reviewDtos;
        }
    }

    public ReviewDto createPersonalReview(ReviewInputDto inputDto, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        Review review = dtoToReview(inputDto);

        if (optionalUser.isPresent()) {
            review.setDate(createDate());
            review.setUser(optionalUser.get());
            reviewRepository.save(review);
            return reviewToDto(review);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
