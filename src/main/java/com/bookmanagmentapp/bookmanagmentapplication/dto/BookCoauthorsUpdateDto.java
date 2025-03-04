package com.bookmanagmentapp.bookmanagmentapplication.dto;

import java.util.List;
import lombok.Data;

@Data
public class BookCoauthorsUpdateDto {
    private List<String> coauthorNames;
}
