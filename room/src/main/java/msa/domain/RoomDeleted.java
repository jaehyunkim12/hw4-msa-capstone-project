package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class RoomDeleted extends AbstractEvent {

    private Long roomId;
    private Boolean status;
    private Integer reivewCnt;
    private String description;

    public RoomDeleted(Room aggregate) {
        super(aggregate);
    }

    public RoomDeleted() {
        super();
    }
    // keep

}
