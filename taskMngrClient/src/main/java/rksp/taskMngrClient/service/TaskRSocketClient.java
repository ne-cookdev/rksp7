package rksp.taskMngrClient.service;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rksp.taskMngrClient.dto.TaskStatusUpdateRequest;
import rksp.taskMngrClient.entity.Task;

@Service
public class TaskRSocketClient {
    private final RSocketRequester requester;

    public TaskRSocketClient(RSocketRequester requester) {
        this.requester = requester;
    }

    public Mono<Task> getTask(Long taskId) {
        return requester.route("tasks.get")
                .data(taskId)
                .retrieveMono(Task.class);
    }

    public Flux<Task> streamTasks() {
        return requester.route("tasks.stream")
                .retrieveFlux(Task.class);
    }

    public Mono<Void> updateTaskStatus(Long taskId, String status) {
        return requester.route("tasks.update")
                .data(new TaskStatusUpdateRequest(taskId, status))
                .send();
    }

    public Flux<Task> createMultipleTasks(Flux<Task> tasks) {
        return requester.route("tasks.create.multiple")
                .data(tasks)
                .retrieveFlux(Task.class);
    }
}
