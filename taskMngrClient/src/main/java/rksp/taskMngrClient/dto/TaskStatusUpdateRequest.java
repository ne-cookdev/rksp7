package rksp.taskMngrClient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskStatusUpdateRequest {
    private Long taskId;
    private String status;
}
