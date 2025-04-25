package no.group.employeemanagement.repository;

import no.group.employeemanagement.model.Employee;
import no.group.employeemanagement.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.id = :employeeId AND YEAR(l.startDate) = :year OR YEAR(l.endDate) = :year")
    List<LeaveRequest> findByEmployeeAndYear(@Param("employeeId") Long employeeId, @Param("year") int year);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.id = :employeeId")
    Collection<Object> findByEmployee(Employee employee);
}
