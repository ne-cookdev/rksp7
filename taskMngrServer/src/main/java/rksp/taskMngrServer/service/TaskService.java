package rksp.taskMngrServer.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rksp.taskMngrServer.entity.Task;
import rksp.taskMngrServer.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Flux<Task> streamTasks() {
        return Flux.fromIterable(taskRepository.findAll());
    }

    public Mono<Task> getTask(Long taskId) {
        return Mono.fromCallable(() -> taskRepository.findById(taskId).
                orElseThrow(() -> new IllegalArgumentException("Task with id " + taskId + " not found!")));
    }

    public Mono<Void> updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task with id " + taskId + " not found!"));
        task.setStatus(status);
        taskRepository.save(task);
        System.out.println("Task with id " + taskId + " updated");
        return Mono.empty();
    }

    public Flux<Task> createMultipleTasks(Flux<Task> tasks) {
        return Flux.from(tasks).flatMap(task -> {
            return Mono.fromCallable(() -> taskRepository.save(task));
        });
    }
}
