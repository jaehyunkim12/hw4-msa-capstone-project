package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class RoomModified extends AbstractEvent {

    private Long roomId;
    private Boolean status;
    private Integer reivewCnt;
    private String description;

    public RoomModified(Room aggregate) {
        super(aggregate);
    }

    public RoomModified() {
        super();
    }
    // keep

}
