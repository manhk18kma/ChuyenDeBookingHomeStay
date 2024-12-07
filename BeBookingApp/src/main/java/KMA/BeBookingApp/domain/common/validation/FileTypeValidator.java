package KMA.BeBookingApp.domain.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<ValidFileType, String> {

    @Override
    public void initialize(ValidFileType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String fileType, ConstraintValidatorContext context) {
//        if (fileType == null || fileType.isEmpty()) {
//            return false;
//        }for (FileType type : FileType.values()) {
//            if (type.getMimeType().equalsIgnoreCase(fileType)) {
//                return true;
//            }
//        }
        return false;
    }
}
