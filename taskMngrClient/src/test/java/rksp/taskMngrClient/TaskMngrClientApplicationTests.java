package rksp.taskMngrClient;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rksp.taskMngrClient.controller.TaskController;
import rksp.taskMngrClient.entity.Task;
import rksp.taskMngrClient.service.TaskRSocketClient;

import java.util.List;

@WebFluxTest(TaskController.class)
class TaskMngrClientApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @SuppressWarnings("removal")
    @MockBean
    private TaskRSocketClient client;

    @Test
    void testGetTaskById() {
        Task task = new Task("Test title", "NEW");

        Mockito.when(client.getTask(1L)).thenReturn(Mono.just(task));

        webTestClient.get()
                .uri("/api/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Test title")
                .jsonPath("$.status").isEqualTo("NEW");
    }

    @Test
    void testGetTaskById_NotFound() {
        Mockito.when(client.getTask(1L))
                .thenReturn(Mono.error(new RuntimeException("Not found")));

        webTestClient.get()
                .uri("/api/tasks/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Not Found");
    }

    @Test
    void testGetAllTasks() {
        List<Task> list = List.of(
                new Task("T1", "NEW"),
                new Task("T2", "DONE")
        );

        Mockito.when(client.streamTasks())
                .thenReturn(Flux.fromIterable(list));

        webTestClient.get()
                .uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("T1")
                .jsonPath("$[1].status").isEqualTo("DONE");
    }

    @Test
    void testCreateTasks() {
        List<Task> tasks = List.of(new Task("A", "NEW"));

        Mockito.when(client.createMultipleTasks(Mockito.any()))
                .thenReturn(Flux.fromIterable(tasks));

        webTestClient.post()
                .uri("/api/tasks")
                .bodyValue(tasks)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("A");
    }

    @Test
    void testUpdateStatus() {
        Mockito.when(client.updateTaskStatus(1L, "DONE"))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/tasks/1")
                .bodyValue("DONE")
                .exchange()
                .expectStatus().isOk();
    }
}
