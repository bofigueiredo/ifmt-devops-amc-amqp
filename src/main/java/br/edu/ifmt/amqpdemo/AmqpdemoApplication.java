package br.edu.ifmt.amqpdemo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



import br.edu.ifmt.amqpdemo.produtor.MessageProducer;

@SpringBootApplication
public class AmqpdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmqpdemoApplication.class, args);
	}

	@Bean
	public RabbitTemplate template(CachingConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	@Bean
	public MessageProducer messageProducer(RabbitTemplate template) { 
		return new MessageProducer(template);
	}

	/*
	 * 
	 *  QUEUES
	 * 
	 */

	@Bean
	public Queue AliceQueue() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", "de.deadletter");
		args.put("x-dead-letter-routing-key", "rk.deadletter");
		args.put("x-message-ttl", 20000);

		return new Queue("q.alice", true, false, false, args);
	}

	@Bean
	public Queue BobQueue() {
		return new Queue("q.bob");
	}

	@Bean
	public Queue CarlaQueue() {
		Map<String, Object> args = new HashMap<>();

		args.put("x-dead-letter-exchange", "de.deadletter");
		args.put("x-dead-letter-routing-key", "rk.deadletter");
		args.put("x-message-ttl", 20000);

		return new Queue("q.carla", true, false, false, args);
	}

	@Bean
	public Queue invalidQueue() {
		return new Queue("q.invalid");
	}

	@Bean
	public Queue deadLetterQueue() {
		return new Queue("q.deadletter"); 
	}

	@Bean
	public Queue whoAmi() {
		return new Queue("q.whoami");
	}

	/*
	 * 
	 *  EXCHANGES
	 * 
	 */

	@Bean
	public FanoutExchange messageFanoutExchange() {
		return new FanoutExchange("fe.message");
	}

	@Bean
	public DirectExchange messDirectExchange() {
		DirectExchange de = new DirectExchange("de.message", true, false);
		de.addArgument("alternate-exchange", "fe.invalid");
		return de;
	}

	@Bean
	public TopicExchange messageTopoExchange() {
		return new TopicExchange("te.message");
	}

	@Bean
	public HeadersExchange messageHeadersExchange() {
		return new HeadersExchange("he.message");
	}

	@Bean
	public FanoutExchange invalidMessFanoutExchange() {
		return new FanoutExchange("fe.invalid");
	}

	@Bean
	public DirectExchange deadLetterDirectExchange() {
		return new DirectExchange("de.deadletter");
	}


	/*
	 * 
	 *  BINDINGS
	 * 
	 */

	@Bean
	public Binding aliceFEBinding(Queue AliceQueue, FanoutExchange messageFanoutExchange) {
		return BindingBuilder.bind(AliceQueue).to(messageFanoutExchange);
	}

	@Bean
	public Binding bobFEBinding(Queue BobQueue, FanoutExchange messageFanoutExchange) {
		return BindingBuilder.bind(BobQueue).to(messageFanoutExchange);
	}

	@Bean
	public Binding carlaFEBinding(Queue CarlaQueue, FanoutExchange messageFanoutExchange) {
		return BindingBuilder.bind(CarlaQueue).to(messageFanoutExchange);
	}

	@Bean
	public Binding aliceDEBinding(Queue AliceQueue, DirectExchange messDirectExchange) {
		return BindingBuilder.bind(AliceQueue).to(messDirectExchange).with("rk.alice");
	}

	@Bean
	public Binding bobDEBinding(Queue BobQueue, DirectExchange messDirectExchange) {
		return BindingBuilder.bind(BobQueue).to(messDirectExchange).with("rk.bob");
	}

	@Bean
	public Binding carlaDEBinding(Queue CarlaQueue, DirectExchange messDirectExchange) {
		return BindingBuilder.bind(CarlaQueue).to(messDirectExchange).with("rk.carla");
	}

	@Bean
	public Binding aliceTEBinding(Queue AliceQueue, TopicExchange messageTopoExchange) {
		return BindingBuilder.bind(AliceQueue).to(messageTopoExchange).with("#.#.mulher");
	}

	@Bean
	public Binding carlaTEBinding(Queue CarlaQueue, TopicExchange messageTopoExchange) {
		return BindingBuilder.bind(CarlaQueue).to(messageTopoExchange).with("#.#.mulher");
	}

	@Bean
	public Binding bobTEBinding(Queue BobQueue, TopicExchange messageTopoExchange) {
		return BindingBuilder.bind(BobQueue).to(messageTopoExchange).with("#.#.homem");
	}

	@Bean
	public Binding aliceHEBinding(Queue AliceQueue, HeadersExchange messageHeadersExchange) {
		Map<String, Object> propriedades = new HashMap<>();
		propriedades.put("x-match", "all");
		propriedades.put("sexo", "feminino");
		propriedades.put("situacao", "ativo");

		return BindingBuilder.bind(AliceQueue).to(messageHeadersExchange).whereAll(propriedades).match();
	}

	@Bean
	public Binding bobHEBinding(Queue BobQueue, HeadersExchange messageHeadersExchange) {
		Map<String, Object> propriedades = new HashMap<>();
		propriedades.put("x-match", "any");
		propriedades.put("sexo", "masculino");
		propriedades.put("situacao", "aposentado");

		return BindingBuilder.bind(BobQueue).to(messageHeadersExchange).whereAny(propriedades).match();
	}

	@Bean
	public Binding carlaHEBinding(Queue CarlaQueue, HeadersExchange messageHeadersExchange) {
		Map<String, Object> propriedades = new HashMap<>();
		propriedades.put("x-match", "all");
		propriedades.put("sexo", "feminino");
		propriedades.put("situacao", "aposentado");

		return BindingBuilder.bind(CarlaQueue).to(messageHeadersExchange).whereAll(propriedades).match();
	}

	@Bean
	public Binding invalidFEBinding(Queue invalidQueue, FanoutExchange invalidMessFanoutExchange) {
		return BindingBuilder.bind(invalidQueue).to(invalidMessFanoutExchange);
	}

	@Bean
	public Binding deadLetterDEBinding(Queue deadLetterQueue, DirectExchange deadLetterDirectExchange) {
		return BindingBuilder.bind(deadLetterQueue).to(deadLetterDirectExchange).with("rk.deadletter");
	}

}
