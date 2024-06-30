package br.edu.ifmt.amqpdemo.consumidor;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rabbitmq.client.Channel;

@Service
@Transactional
public class Carla {
    
    @RabbitListener(queues = "q.carla", ackMode = "MANUAL")
    public void recebeMensagem(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws InterruptedException, IOException  {
        Thread.sleep(6000);
        System.out.println("CARLA: " + msg);
        channel.basicAck(tag, false);
    }

}
