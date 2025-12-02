package rksp.taskMngrServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rksp.taskMngrServer.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);

    List<Task> findByStatus(String status);
}
