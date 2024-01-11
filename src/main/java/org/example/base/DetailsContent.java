package org.example.base;

import lombok.Data;

import java.util.List;

@Data
public class DetailsContent {
    private final String title;
    private final String inner;
    private final List<String> objects;
    private String translated;
}
