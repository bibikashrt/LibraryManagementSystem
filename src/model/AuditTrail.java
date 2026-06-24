package model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditTrail {

    private String createdBy;
    private LocalDateTime createdOn;

    private String updatedBy;
    private LocalDateTime updatedOn;
}

//     public String getCreatedBy() {
//         return createdBy;
//     }
//     public void setCreatedBy(String createdBy) {
//         this.createdBy = createdBy;
//     }
//     public LocalDateTime getCreatedOn() {
//         return createdOn;
//     }
//     public void setCreatedOn(LocalDateTime createdOn) {
//         this.createdOn = createdOn;
//     }
//     public String getUpdatedBy() {
//         return updatedBy;
//     }
//     public void setUpdatedBy(String updatedBy) {
//         this.updatedBy = updatedBy;
//     }
//     public LocalDateTime getUpdatedOn() {
//         return updatedOn;
//     }
//     public void setUpdatedOn(LocalDateTime updatedOn) {
//         this.updatedOn = updatedOn;
//     }

// }
