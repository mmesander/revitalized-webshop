package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.ReviewInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ReviewPatchInputDto;
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
import nu.revitalized.revitalizedwebshop.specifications.ReviewSpecification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CheckIfUserHasItem.checkIfUserHasReview;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.CreateDate.createDate;
import static nu.revitalized.revitalizedwebshop.helpers.FormatDate.formatDate;
import static nu.revitalized.revitalizedwebshop.helpers.UpdateRating.updateGarmentRating;
import static nu.revitalized.revitalizedwebshop.helpers.UpdateRating.updateSupplementRating;
import static nu.revitalized.revitalizedwebshop.services.GarmentService.garmentToDto;
import static nu.revitalized.revitalizedwebshop.services.SupplementService.supplementToDto;

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
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

        return reviewToDto(review);
    }

    public List<ReviewDto> getReviewsByParam(
            Integer minRating,
            Integer maxRating
    ) {
        ReviewSpecification params = new ReviewSpecification(minRating, maxRating);

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
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

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
    }

    public ReviewDto patchReview(Long id, ReviewPatchInputDto inputDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

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
    }

    public String deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

        if (review.getUser() != null) {
            User user = review.getUser();
            List<Review> userReviews = user.getReviews();
            userReviews.remove(review);
            user.setReviews(userReviews);
            userRepository.save(user);
        }

        if (review.getSupplement() != null) {
            supplementRepository.save(updateSupplementRating(review, review.getSupplement(), true, false));
            reviewRepository.deleteById(id);

            return "Review with id: " + id + " from: " + formatDate(review.getDate())
                    + " is removed from Supplement: " + review.getSupplement().getName() + " with id: "
                    + review.getSupplement().getId();
        } else if (review.getGarment() != null) {
            garmentRepository.save(updateGarmentRating(review, review.getGarment(), true, false));
            reviewRepository.deleteById(id);

            return "Review with id: " + id + " from: " + formatDate(review.getDate())
                    + " is removed from Garment: " + review.getGarment().getName() + " with id: "
                    + review.getGarment().getId();
        } else {
            reviewRepository.deleteById(id);

            return "Review with id: " + id + " from: " + formatDate(review.getDate()) + " is removed";
        }
    }

    // Relation - Product Methods
    public List<ReviewDto> getAllReviewsFromProduct(Long productId) {
        List<Review> reviews;

        if (supplementRepository.existsById(productId)) {
            Supplement supplement = supplementRepository.findById(productId).orElseThrow();
            reviews = supplement.getReviews();
        } else if (garmentRepository.existsById(productId)) {
            Garment garment = garmentRepository.findById(productId).orElseThrow();
            reviews = garment.getReviews();
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }

        if (reviews.isEmpty()) {
            throw new RecordNotFoundException("No reviews found for product with id: " + productId);
        }

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            ReviewDto reviewDto = reviewToDto(review);
            reviewDtos.add(reviewDto);
        }
        reviewDtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());

        return reviewDtos;
    }

    public Object assignReviewToProduct(Long productId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", reviewId)));

        if (review.getSupplement() != null || review.getGarment() != null) {
            String type = review.getSupplement() != null ? "supplement" : "garment";
            String name = review.getSupplement() != null ? review.getSupplement().getName() : review.getGarment().getName();
            Long id = review.getSupplement() != null ? review.getSupplement().getId() : review.getGarment().getId();

            throw new BadRequestException("Review with id: " + reviewId + " is already assigned to " + type
                    + ": " + name + " with id: " + id);
        }

        Object objectDto;

        if (supplementRepository.existsById(productId)) {
            Supplement supplement = supplementRepository.findById(productId).orElseThrow();
            supplementRepository.save(updateSupplementRating(review, supplement, false, false));
            objectDto = supplementToDto(supplement);
        } else if (garmentRepository.existsById(productId)) {
            Garment garment = garmentRepository.findById(productId).orElseThrow();
            garmentRepository.save(updateGarmentRating(review, garment, false, false));
            objectDto = garmentToDto(garment);
        } else {
            throw new BadRequestException(buildIdNotFound("Product", productId));
        }

        reviewRepository.save(review);

        return objectDto;
    }

    public Object removeReviewFromProduct(Long productId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", reviewId)));

        Object objectDto;

        if (supplementRepository.existsById(productId)) {
            Supplement supplement = supplementRepository.findById(productId).orElseThrow();
            if (review.getSupplement() == null || !review.getSupplement().equals(supplement)) {
                throw new BadRequestException("Review with id: " + reviewId
                        + " is not assigned to product: " + productId);
            }
            supplementRepository.save(updateSupplementRating(review, supplement, true, false));
            reviewRepository.deleteById(reviewId);
            objectDto = supplementToDto(supplement);
        } else if (garmentRepository.existsById(productId)) {
            Garment garment = garmentRepository.findById(productId).orElseThrow();
            if (review.getGarment() == null || !review.getGarment().equals(garment)) {
                throw new BadRequestException("Review with id: " + reviewId
                        + " is not assigned to product: " + productId);
            }
            garmentRepository.save(updateGarmentRating(review, garment, true, false));
            reviewRepository.deleteById(reviewId);
            objectDto = garmentToDto(garment);
        } else {
            throw new BadRequestException(buildIdNotFound("Product", productId));
        }

        return objectDto;
    }

    // Relation - Authenticated User Methods
    public List<ReviewDto> getAllAuthUserReviews(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<Review> reviews = user.getReviews();
        List<ReviewDto> reviewDtos = new ArrayList<>();

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

    public ReviewDto getAuthUserReviewById(String username, Long id) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

        List<Review> reviews = user.getReviews();
        ReviewDto dto = null;

        for (Review foundReview : reviews) {
            if (foundReview.equals(review)) {
                dto = reviewToDto(review);
            }
        }

        if (dto == null) {
            throw new BadRequestException("User: " + username + " does not have review with id: " + id);
        }

        return dto;
    }

    public ReviewDto createAuthUserReview(ReviewInputDto inputDto, String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Review review = dtoToReview(inputDto);
        review.setDate(createDate());
        review.setUser(user);
        reviewRepository.save(review);

        return reviewToDto(review);
    }

    public ReviewDto updateAuthUserReview(String username, Long id, ReviewInputDto inputDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

        checkIfUserHasReview(user, review);

        return updateReview(id, inputDto);
    }

    public ReviewDto patchAuthUserReview(String username, Long id, ReviewPatchInputDto inputDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

        checkIfUserHasReview(user, review);

        return patchReview(id, inputDto);
    }

    public String deleteAuthUserReview(String username, Long id) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Review", id)));

        checkIfUserHasReview(user, review);

        return deleteReview(id);
    }
}