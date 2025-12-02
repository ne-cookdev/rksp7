package rksp.taskMngrClient.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    @GetMapping("/")
    public Flux<Task> getTasksByStatus(@RequestParam String status) {
        return client.streamTasksByStatus(status).onErrorMap(ex -> new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", ex));
    }

    @GetMapping("/delay")
    public Flux<Task> getTasksByStatusDelay(@RequestParam String status) {
        return client.streamTasksByStatusDelay(status).onErrorMap(ex -> new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", ex));
    }

    @GetMapping("/{id}")
    public Mono<Task> getTask(@PathVariable Long id) {
        return client.getTask(id).onErrorMap(ex -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task not found", ex));
    }

    @GetMapping
    public Flux<Task> getAllTasks() {
        return client.streamTasks().onErrorMap(ex -> new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", ex));
    }

    @PostMapping
    public Flux<Task> createTasks(@RequestBody List<Task> tasks) {
        Flux<Task> flux = Flux.fromIterable(tasks);
        return client.createMultipleTasks(flux).onErrorMap(ex -> new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", ex));
    }

    @PutMapping("/{id}")
    public Mono<Void> updateStatus(@PathVariable Long id, @RequestBody String status) {
        return client.updateTaskStatus(id, status).onErrorMap(ex -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task not found", ex));
    }
}
