package com.company.models.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    HttpStatus status;
    String message;
    T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .status(HttpStatus.OK)
                .message("success")
                .data(data)
                .build();
    }

    public static BaseResponse<Void> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> error(T data) {
        return BaseResponse.<T>builder()
                .status(HttpStatus.OK)
                .message("error")
                .data(data)
                .build();
    }

}
