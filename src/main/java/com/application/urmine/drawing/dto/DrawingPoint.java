package com.application.urmine.drawing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingPoint {

    private double x;
    private double y;

    private String color;

    private double strokeWidth;
}
