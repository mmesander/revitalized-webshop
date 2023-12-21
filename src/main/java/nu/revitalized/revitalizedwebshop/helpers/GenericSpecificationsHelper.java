//package nu.revitalized.revitalizedwebshop.helpers;
//
//// Imports
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Predicate;
//
//@Getter
//@Setter
//public class GenericSpecificationsHelper {
//    private final Object criteria;
//
//    public GenericSpecificationsHelper(Object criteria) {
//        this.criteria = criteria;
//    }
//
//    public static Specification toPredicate(Object criteria) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            for (Field field : criteria.getClass().getDeclaredFields()) {
//                try {
//                    Object value = field.get(criteria);
//                    if (value != null) {
//                        if (field.getType().equals(Double.class)) {
//                            predicates.add(criteriaBuilder.lessThanOrEqualTo())
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
