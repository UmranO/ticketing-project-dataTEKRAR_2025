package com.cydeo.repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    //Asagida based on projectCode we counted the Unfinished and Finished Tasks that belong to that project

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectCode = ?1 AND t.taskStatus <> 'COMPLETE'")
    int totalNonCompletedTasks(String projectCode);
//----------------------------------------------------------------------------------------------------------------------

    @Query(value = "SELECT COUNT(*) " +
            "FROM tasks t JOIN projects p on t.project_id=p.id " +
            "WHERE p.project_code=?1 AND t.task_status='COMPLETE'",nativeQuery = true)

//----------------------------------------------------------------------------------------------------------------------
    int totalCompletedTasks(String projectCode);
//----------------------------------------------------------------------------------------------------------------------
    List<Task> findAllByProject(Project project);
//----------------------------------------------------------------------------------------------------------------------
    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);
//------------------------------------------------------------------------------------
//Asagisi benim Pending Task icin yaptigim. CT farkli yapmis. Bunu comment edecegim yukardaki CT'nin ki. O hem status
//hem de employee'yi secen bir () create etmis. Ben status'u stream ile filter ederek yaptim TaskServiceImpl'de. fark bu

    //List<Task> findAllByAssignedEmployee(User employee);
//----------------------------------------------------------------------------------------------------------------------

    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);


}
