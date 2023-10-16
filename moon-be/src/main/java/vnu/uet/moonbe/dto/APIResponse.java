package vnu.uet.moonbe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> APIResponse<T> of(T data) {
        return new APIResponse<>(200, HttpStatus.valueOf(200).getReasonPhrase(), data);
    }

    public static <T> APIResponse<T> err(HttpStatus status, String errMessage) {
        return new APIResponse<>(status.value(), errMessage, null);
    }
}
