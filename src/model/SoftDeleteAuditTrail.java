package model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SoftDeleteAuditTrail extends AuditTrail {

    private String deletedBy;
    private LocalDateTime deletedOn;

    // public String getDeletedBy() {
    //     return deletedBy;
    // }
    // public void setDeletedBy(String deletedBy) {
    //     this.deletedBy = deletedBy;
    // }
    // public LocalDateTime getDeletedOn() {
    //     return deletedOn;
    // }
    // public void setDeletedOn(LocalDateTime deletedOn) {
    //     this.deletedOn = deletedOn;
    // }
}
