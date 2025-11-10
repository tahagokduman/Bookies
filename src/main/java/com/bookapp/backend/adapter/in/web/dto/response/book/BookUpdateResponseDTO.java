package dto.response.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookUpdateResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private String description;
    private String coverImageUrl;
    private Integer pageCount;
    private String publisher;
    private Integer publishedYear;
    private LocalDateTime updatedAt;
    private String genre;
    private String language;
}
