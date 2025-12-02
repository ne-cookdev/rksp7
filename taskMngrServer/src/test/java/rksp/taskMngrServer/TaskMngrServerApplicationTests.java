package rksp.taskMngrServer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import rksp.taskMngrServer.dto.TaskStatusUpdateRequest;
import rksp.taskMngrServer.entity.Task;
import rksp.taskMngrServer.repository.TaskRepository;

@SpringBootTest
class TaskMngrServerApplicationTests {
    private RSocketRequester requester;

    @Autowired
    private RSocketStrategies rSocketStrategies;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup(@Autowired RSocketRequester.Builder builder) {
        this.requester = builder
                .rsocketStrategies(rSocketStrategies)
                .connectTcp("localhost", 7000)
                .block();
    }

    @Test
    void testRequestResponse_getTask() {
        Task saved = taskRepository.save(new Task(null, "Test task", "CREATED"));

        Mono<Task> response = requester.route("tasks.get")
                .data(saved.getId())
                .retrieveMono(Task.class);

        StepVerifier.create(response)
                .expectNextMatches(task -> task.getTitle().equals("Test task"))
                .verifyComplete();
    }

    @Test
    void testRequestChannel_createMultipleTasks() {
        Flux<Task> tasks = Flux.just(
                new Task(null, "Task A", "CREATED"),
                new Task(null, "Task B", "CREATED")
        );

        Flux<Task> response = requester.route("tasks.create.multiple")
                .data(tasks)
                .retrieveFlux(Task.class);

        StepVerifier.create(response)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testFireAndForget() throws InterruptedException {
        Mono<Void> result = requester.route("tasks.update")
                .data(new TaskStatusUpdateRequest(2L, "DONE"))
                .send();

        StepVerifier.create(result)
                .verifyComplete();

        Thread.sleep(1000);

        Task createdTask = taskRepository.findByTitle("task2");
        Assertions.assertEquals("DONE", createdTask.getStatus());
    }

    @Test
    void testRequestStream() {
        // убедимся, что есть данные
        taskRepository.save(new Task(null, "Stream1", "READY"));
        taskRepository.save(new Task(null, "Stream2", "READY"));

        Flux<Task> stream = requester.route("tasks.stream")
                .retrieveFlux(Task.class);

        StepVerifier.create(stream)
                .expectNextCount(2)
                .thenCancel()
                .verify();
    }
}
