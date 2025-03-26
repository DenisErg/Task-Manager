package sk.edenis.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import sk.edenis.taskmanager.constant.TaskConsts;
import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.dto.TaskDTO;
import sk.edenis.taskmanager.exception.MissingFieldException;
import sk.edenis.taskmanager.model.Task;
import sk.edenis.taskmanager.model.User;
import sk.edenis.taskmanager.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public TaskDTO createTask(TaskDTO taskDTO) {
		Task task = new Task();	
		
		validateTaskDto(taskDTO);
	
		if(taskDTO.getStartTime() != null) {
			task.setStartTime(taskDTO.getStartTime());
		}
		
		task.setDeadline(taskDTO.getDeadline());
		task.setUser(getCurrentUser());
		task.setDescription(taskDTO.getDescription());
		task.setPriority(taskDTO.getPriority());
		task.setFinished(false);

		return toDTO(taskRepository.save(task));
	}

	@Override
	public TaskDTO getTask(UUID id) {	
		
		if(id == null) {
			throw new MissingFieldException("Task id");
		}
		
		try {
			Task task = taskRepository.findByTaskIdAndUser(id, getCurrentUser()).get(); 
			return toDTO(task);
		} catch(NumberFormatException e) {
			throw new NumberFormatException("Invalid number format. Please enter a valid numeric value.");
		} catch(NoSuchElementException e) {
			throw new NoSuchElementException(TaskConsts.TASK_NOT_FOUND_MESSAGE);
		}
	}

	@Override
	public List<TaskDTO> getAllTasks() {	
		List<Task> tasks = taskRepository.findAllByUser(getCurrentUser());
		if(tasks.isEmpty()) {
			throw new NoSuchElementException(TaskConsts.NO_TASKS_MESSAGE);
		}		
		return toDTOs(tasks);
	}

	@Override
	public TaskDTO updateTask(UUID id, TaskDTO taskDTO) {
		Task task = new Task();
		
		try {
			task = taskRepository.findByTaskIdAndUser(id,getCurrentUser()).get();	
		} catch(NoSuchElementException e) {
			throw new NoSuchElementException(TaskConsts.TASK_NOT_FOUND_MESSAGE);
		}
		
		validateTaskDto(taskDTO);	
	
		if(taskDTO.getStartTime() != null) {
			task.setStartTime((taskDTO.getStartTime()));
		}
		
		if (taskDTO.getFinished() == null) {
		    taskDTO.setFinished(false);
		}
		
		task.setDeadline(taskDTO.getDeadline());
		task.setUser(getCurrentUser());
		task.setDescription(taskDTO.getDescription());
		task.setPriority(taskDTO.getPriority());
		task.setFinished(taskDTO.getFinished());

		return toDTO(taskRepository.save(task));
	}

	@Override
	public void deleteTask(UUID id) {
		taskRepository.deleteById(id);

	}
	
	private void validateTaskDto(TaskDTO dto) {
	    if(dto.getDescription().isBlank()) throw new MissingFieldException("Description");
	    if(dto.getDeadline() == null) throw new MissingFieldException("Deadline");
	}
	
	private TaskDTO toDTO(Task task) {
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(task.getTaskId().toString());
		taskDTO.setDescription(task.getDescription());
		taskDTO.setPriority(task.getPriority());
		taskDTO.setStartTime(task.getStartTime());
		taskDTO.setDeadline(task.getDeadline());		
		taskDTO.setFinished(task.isFinished());
		return taskDTO;
	}
	
	private List<TaskDTO> toDTOs(List<Task> tasks) {
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		
		for(Task task: tasks) {			
			TaskDTO taskDTO = toDTO(task);			
			taskDTOs.add(taskDTO);
		}
		
		return taskDTOs;
	}
	
	private User getCurrentUser() {
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	    if (principal instanceof UserDetails) {
	        return (User) principal;
	    } else {
	        throw new RuntimeException(UserConsts.USER_UNAUTHENTICATED_MESSAGE);
	    }
	}
}
