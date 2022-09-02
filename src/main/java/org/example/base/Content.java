package org.example.base;

import lombok.*;

import java.util.List;

@Data
public class Content {
     private final String title;
     private final String inner;
     private final List<String> href;
     private String translated;
}
