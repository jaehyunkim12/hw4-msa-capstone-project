package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class RoomReserved extends AbstractEvent {

    private Long roomId;
    private Boolean status;
    private Integer reviewCnt;
    private String description;

    public RoomReserved(Room aggregate) {
        super(aggregate);
    }

    public RoomReserved() {
        super();
    }
    // keep

}
