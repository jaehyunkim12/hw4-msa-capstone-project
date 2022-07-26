package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class RoomCancelled extends AbstractEvent {

    private Long roomId;
    private Boolean status;
    private Integer reviewCnt;
    private String description;

    public RoomCancelled(Room aggregate) {
        super(aggregate);
    }

    public RoomCancelled() {
        super();
    }
    // keep

}
