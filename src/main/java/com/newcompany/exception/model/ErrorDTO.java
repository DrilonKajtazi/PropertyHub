package com.newcompany.exception.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
    public String status;
    public String message;
    public String time;

}
