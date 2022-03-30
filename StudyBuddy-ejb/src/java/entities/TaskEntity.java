package entities;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.Function;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;



@Entity

public class TaskEntity extends AbstractAuditableEntity{

    
    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    
    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdDate;
    
    @Column(nullable = false)
    @NotNull
    private LocalDateTime lastModifiedDate;
    
        
    public static enum Status {
        TODO, DOING, DONE;
    }

    public static Comparator<TaskEntity> DEFAULT_COMPARATOR = Comparator
            .comparing(TaskEntity::getCreatedDate).reversed()
            .thenComparing(TaskEntity::getName)
            .thenComparing(TaskEntity::getDescription);

    public static Function<TaskEntity, String> TO_STRING = t
            -> "Task[name:" + t.getName()
            + "\n description:" + t.getDescription()
            + "\n status:" + t.getStatus()
            + "\n createdAt:" + t.getCreatedDate()
            + "\n lastModifiedAt:" + t.getLastModifiedDate()
            + "]";

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the createdDate
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the lastModifiedDate
     */
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }



}
