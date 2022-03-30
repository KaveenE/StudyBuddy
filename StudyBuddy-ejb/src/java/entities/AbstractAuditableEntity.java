package entities;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;



@MappedSuperclass

//@EntityListeners(AuditEntityListener.class)
public class AbstractAuditableEntity extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "created_at")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedDate;

//    @ManyToOne()
//    @JoinColumn(name="created_by")
    @Column(name = "created_by")
    private String createdBy;

//    @ManyToOne()
//    @JoinColumn(name="updated_by")
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}