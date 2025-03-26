package sk.edenis.taskmanager.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sk.edenis.taskmanager.model.Task;
import sk.edenis.taskmanager.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

	Optional<Task> findByTaskIdAndUser(UUID taskId, User user);
	
	List<Task> findAllByUser(User user);
}
