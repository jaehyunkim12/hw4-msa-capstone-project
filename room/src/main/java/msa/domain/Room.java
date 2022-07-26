package msa.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import msa.RoomApplication;
import msa.domain.RoomCancelled;
import msa.domain.RoomDeleted;
import msa.domain.RoomModified;
import msa.domain.RoomRegistered;
import msa.domain.RoomReserved;

@Entity
@Table(name = "Room_table")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;

    private Boolean status;

    private Integer reviewCnt;

    private String description;

    @PostPersist
    public void onPostPersist() {
        RoomRegistered roomRegistered = new RoomRegistered(this);
        roomRegistered.publishAfterCommit();

        RoomModified roomModified = new RoomModified(this);
        roomModified.publishAfterCommit();

        RoomDeleted roomDeleted = new RoomDeleted(this);
        roomDeleted.publishAfterCommit();

        RoomReserved roomReserved = new RoomReserved(this);
        roomReserved.publishAfterCommit();

        RoomCancelled roomCancelled = new RoomCancelled(this);
        roomCancelled.publishAfterCommit();
    }

    public static RoomRepository repository() {
        RoomRepository roomRepository = RoomApplication.applicationContext.getBean(
            RoomRepository.class
        );
        return roomRepository;
    }

    public static void updateReview(ReviewCreated reviewCreated) {
        /** Example 1:  new item 
        Room room = new Room();
        repository().save(room);

        */

        /** Example 2:  finding and process
        
        repository().findById(reviewCreated.get???()).ifPresent(room->{
            
            room // do something
            repository().save(room);


         });
        */

    }

    public static void updateReview(ReviewDeleted reviewDeleted) {
        /** Example 1:  new item 
        Room room = new Room();
        repository().save(room);

        */

        /** Example 2:  finding and process
        
        repository().findById(reviewDeleted.get???()).ifPresent(room->{
            
            room // do something
            repository().save(room);


         });
        */

    }

    public static void confirmReserve(
        ReservationConfirmed reservationConfirmed
    ) {
        /** Example 1:  new item 
        Room room = new Room();
        repository().save(room);

        */

        /** Example 2:  finding and process
        
        repository().findById(reservationConfirmed.get???()).ifPresent(room->{
            
            room // do something
            repository().save(room);


         });
        */

    }

    public static void cancel(ReservationCancelled reservationCancelled) {
        /** Example 1:  new item 
        Room room = new Room();
        repository().save(room);

        */

        /** Example 2:  finding and process
        
        repository().findById(reservationCancelled.get???()).ifPresent(room->{
            
            room // do something
            repository().save(room);


         });
        */

    }
}
