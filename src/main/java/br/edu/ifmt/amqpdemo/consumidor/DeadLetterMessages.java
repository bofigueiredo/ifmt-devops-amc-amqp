package br.edu.ifmt.amqpdemo.consumidor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeadLetterMessages {
    
    @RabbitListener(queues = "q.deadletter")
    public void recebeMensagem(String msg) throws InterruptedException  {
        Thread.sleep(6000);
        System.out.println("DEAD-LETTER (TTL): " + msg);
    }

}
