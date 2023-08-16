package com.exmple.task.dto.request.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateMasterInfoRequest {
    private String title;
    private String text;
}
