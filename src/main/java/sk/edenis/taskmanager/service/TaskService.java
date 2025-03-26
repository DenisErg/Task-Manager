package sk.edenis.taskmanager.service;

import java.util.List;
import java.util.UUID;

import sk.edenis.taskmanager.dto.TaskDTO;

public interface TaskService {

	public TaskDTO createTask(TaskDTO taskDTO);

	public TaskDTO getTask(UUID id);

	public List<TaskDTO> getAllTasks();

	public TaskDTO updateTask(UUID id,TaskDTO taskDTO);

	public void deleteTask(UUID id);

}
