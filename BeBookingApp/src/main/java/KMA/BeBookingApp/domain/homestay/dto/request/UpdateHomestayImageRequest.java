package KMA.BeBookingApp.domain.homestay.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpdateHomestayImageRequest implements Serializable {
    @NotEmpty(message = "Content cannot be empty") // Đảm bảo content không rỗng
    private String content;

    @NotNull(message = "Media removed IDs cannot be null") // Đảm bảo mediaRemovedIds không null
    private List<Long> mediaRemovedIds;
}
