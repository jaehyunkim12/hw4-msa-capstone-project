package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class RoomRegistered extends AbstractEvent {

    private Long roomId;
    private Boolean stauts;
    private Integer reivewCnt;
    private String descrition;

    public RoomRegistered(Room aggregate) {
        super(aggregate);
    }

    public RoomRegistered() {
        super();
    }
    // keep

}
