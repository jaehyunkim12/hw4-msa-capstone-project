package msa.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import msa.config.kafka.KafkaProcessor;
import msa.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RoomviewViewHandler {

    @Autowired
    private RoomviewRepository roomviewRepository;
    // keep

}
