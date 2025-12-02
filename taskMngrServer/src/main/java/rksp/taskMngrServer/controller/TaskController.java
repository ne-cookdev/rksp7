package rksp.taskMngrServer.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rksp.taskMngrServer.dto.TaskStatusUpdateRequest;
import rksp.taskMngrServer.entity.Task;
import rksp.taskMngrServer.service.TaskService;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @MessageMapping("tasks.stream")
    public Flux<Task> streamTasks() {
        return taskService.streamTasks();
    }

    @MessageMapping("tasks.create.multiple")
    public Flux<Task> createTasks(Flux<Task> tasks) {
        return taskService.createMultipleTasks(tasks);
    }

    @MessageMapping("tasks.get")
    public Mono<Task> getTask(Long taskId) {
        System.out.println(taskId);
        return taskService.getTask(taskId);
    }

    @MessageMapping("tasks.update")
    public Mono<Void> updateTaskStatus(TaskStatusUpdateRequest req) {
        return taskService.updateTaskStatus(req.getTaskId(), req.getStatus());
    }
}
