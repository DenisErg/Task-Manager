package sk.edenis.taskmanager.constant;

public class TaskConsts {
	
	//HTML NAMES
	public static final String TASK_LIST_HTML 			= "task-list";
	public static final String TASK_FORM_HMTL			= "task-form";
	
	//URLs
	public static final String TASK_LIST_URL 			= "/task-list";
	public static final String CREATE_TASK_URL 			= "/create-task";
	public static final String EDIT_TASK_URL			= "/edit-task/{id}";
	public static final String DELETE_TASK_URL 			= "/delete-task/{id}";
	
	//MESSAGES
    public static final String NO_TASKS_MESSAGE 		= "You have no tasks. Create a new one to get started!"; 
    public static final String TASK_CREATED_MESSAGE 	= "Task created.";
    public static final String TASK_UPDATED_MESSAGE 	= "Task updated.";
    public static final String TASK_DELETED_MESSAGE 	= "Task has been deleted.";
    public static final String TASK_NOT_FOUND_MESSAGE 	= "Task not found";

}
