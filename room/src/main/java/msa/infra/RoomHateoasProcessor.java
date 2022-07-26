package msa.infra;

import msa.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class RoomHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Room>> {

    @Override
    public EntityModel<Room> process(EntityModel<Room> model) {
        return model;
    }
}
