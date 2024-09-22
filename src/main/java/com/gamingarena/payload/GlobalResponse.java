package com.gamingarena.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse {

    private String msg;
    private Object data;
    private String err;
    private RespStatusEnum status;
}