package sk.edenis.taskmanager.controller.mvc;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import sk.edenis.taskmanager.constant.TaskConsts;
import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.dto.TaskDTO;
import sk.edenis.taskmanager.util.ControllerUtil;
import sk.edenis.taskmanager.exception.MissingFieldException;

@Controller
public class TaskMvcController {

    private final WebClient webClient;
    
    private static final Logger logger = LoggerFactory.getLogger(TaskMvcController.class);

    public TaskMvcController(WebClient.Builder webClientBuilder, @Value("${task.api.base-url}") String taskApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(taskApiBaseUrl).build();
    }
    

    @GetMapping(TaskConsts.CREATE_TASK_URL)
    public String createTask() {
    	return TaskConsts.TASK_FORM_HMTL;
    }
    
    @PostMapping(TaskConsts.CREATE_TASK_URL)
    public RedirectView saveTask(@ModelAttribute TaskDTO taskDTO, RedirectAttributes attributes, HttpServletRequest request) {
        try {
           	
        	String jwt = ControllerUtil.getJWT(request);
  
        	logger.info("📡 Sending save task request: POST /tasks with JWT: {}", jwt);
        	
             webClient.post()
            	.cookies(cookies -> cookies.add(UserConsts.JWT_COOKIE_NAME,jwt))
                .bodyValue(taskDTO)
                .exchangeToMono(response -> {
                	return ControllerUtil.handleResponse(response, MissingFieldException::new);
                })
     		    .block();
            
            return new RedirectView(TaskConsts.TASK_LIST_URL);
        } catch (Exception e) {
        	logger.error("saveTask", e.getMessage());
        	attributes.addFlashAttribute("Save task error: " + e.getMessage());
            return new RedirectView(TaskConsts.CREATE_TASK_URL);
        }
    }
    
    @GetMapping(TaskConsts.TASK_LIST_URL)
    public String getTasks(Model model, HttpServletRequest request) {
        try {
        	
        	String jwt = ControllerUtil.getJWT(request);
        	
        	logger.info("📡 Sending get all user tasks request: GET /tasks with JWT: {}", jwt);
        	
            List<TaskDTO> tasks = webClient.get()
            	.cookies(cookies -> cookies.add(UserConsts.JWT_COOKIE_NAME,jwt))
                .retrieve()
                .bodyToFlux(TaskDTO.class)              
                .collectList()
                .block();
            
            model.addAttribute("tasks", tasks);
        } catch (WebClientResponseException.NotFound e) {
        	model.addAttribute("noTasks",TaskConsts.NO_TASKS_MESSAGE);
        } catch (Exception e) {
        	model.addAttribute("error", e.getMessage());           
        }
        
        return TaskConsts.TASK_LIST_HTML;
    }

    @GetMapping(TaskConsts.EDIT_TASK_URL)
    public String getTask(@PathVariable UUID id, Model model, HttpServletRequest request) {
        try {
        	
        	String jwt = ControllerUtil.getJWT(request);
        	  
        	logger.info("📡 Sending get task request: GET /tasks with JWT: {}", jwt);
        	
            TaskDTO task = webClient.get()
                .uri("/{id}", id)
                .cookies(cookies -> cookies.add(UserConsts.JWT_COOKIE_NAME,jwt))
                .retrieve()
                .bodyToMono(TaskDTO.class)
                .block();

            model.addAttribute("task", task);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        
        return TaskConsts.TASK_FORM_HMTL;
    }
    
    @PostMapping(TaskConsts.EDIT_TASK_URL)
    public RedirectView updateTask(@PathVariable UUID id, @ModelAttribute TaskDTO taskDTO, RedirectAttributes attributes, HttpServletRequest request) {
        try {
        	String jwt = ControllerUtil.getJWT(request);
        	  
        	logger.info("📡 Sending update task request: PUT /tasks/" + id + " with JWT: {}", jwt);
        	
        	 webClient.put()
        	 	.uri("/" + id)
         	 	.cookies(cookies -> cookies.add(UserConsts.JWT_COOKIE_NAME,jwt))
         	 	.bodyValue(taskDTO)
         	 	.retrieve()
         	 	.bodyToMono(TaskDTO.class)
         	 	.block();
        	return new RedirectView(TaskConsts.TASK_LIST_URL);
        } catch (Exception e) {
        	attributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/edit-task/" + id);
        }
    }

    
    @PostMapping(TaskConsts.DELETE_TASK_URL)
    public RedirectView deleteTask(@PathVariable UUID id, HttpServletRequest request, RedirectAttributes attributes) {
    	try {
           	
        	String jwt = ControllerUtil.getJWT(request);
  
        	logger.info("📡 Sending delete task request: DELETE /tasks with JWT: {}", jwt);
        	
             webClient.delete()
             	.uri("/" + id)
             	.cookies(cookies -> cookies.add(UserConsts.JWT_COOKIE_NAME, jwt))
                .retrieve()
                .toBodilessEntity()
                .block();              
        } catch (Exception e) {
        	attributes.addFlashAttribute("error", e.getMessage());
        }
    	
    	return new RedirectView(TaskConsts.TASK_LIST_URL);
    }
}
