package com.zhaomsdemo.research.testcontainer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Result<T> {

    @Id
    String id;
    Integer code;
    String message;
    T data;
}
