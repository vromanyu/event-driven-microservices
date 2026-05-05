package com.vromanyu.order;

import com.vromanyu.event.OrderCreatedEvent;
import com.vromanyu.outbox.Outbox;
import com.vromanyu.outbox.OutboxDao;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
class OrderPoller {

    @Inject
    OutboxDao outboxDao;

    @Inject
    @Channel("order-out-events")
    MutinyEmitter<OrderCreatedEvent> emitter;

    @Transactional
    @Scheduled(every = "5s", delay = 10, delayUnit = TimeUnit.SECONDS)
    void process() {
        Log.infof("%s - Starting processing batch", OffsetDateTime.now());
        List<Outbox> first10Unprocessed = outboxDao.findFirst10Unprocessed();
        Log.infof("Found %d unprocessed outbox records", first10Unprocessed.size());
        first10Unprocessed.forEach(r -> {
            Log.infof("Processing outbox record: %s", r.getId());
            try {
                emitter.sendMessageAndAwait(Message.of(r.getPayload(), Metadata.of(OutgoingKafkaRecordMetadata.<String>builder().withKey(r.getId().toString()).build())));
                r.setProcessed("1");
                outboxDao.update(r);
                Log.infof("Outbox record id: %s - processed", r.getId());
            } catch (Exception e) {
                Log.errorf("Error processing outbox record id: %s", r.getId(), e);
            }
        });
    }
}
