package rksp.taskMngrClient.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rksp.taskMngrClient.entity.Task;
import rksp.taskMngrClient.service.TaskRSocketClient;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRSocketClient client;

    public TaskController(TaskRSocketClient client) {
        this.client = client;
    }

    @GetMapping("/{id}")
    public Mono<Task> getTask(@PathVariable Long id) {
        return client.getTask(id);
    }

    @GetMapping
    public Flux<Task> getAllTasks() {
        return client.streamTasks();
    }

    @PostMapping
    public Flux<Task> createTask(@RequestBody List<Task> tasks) {
        Flux<Task> flux = Flux.fromIterable(tasks);
        return client.createMultipleTasks(flux);
    }

    @PutMapping("/{id}")
    public Mono<Void> updateStatus(@PathVariable Long id, @RequestBody String status) {
        return client.updateTaskStatus(id, status);
    }
}
