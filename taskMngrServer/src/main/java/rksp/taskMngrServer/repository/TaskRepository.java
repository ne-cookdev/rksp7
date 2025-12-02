package rksp.taskMngrServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rksp.taskMngrServer.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);
}
