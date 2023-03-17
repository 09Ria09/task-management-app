package server.database;

import commons.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<TaskList, Long> {}
