package infnet.pb.RSS.Bank.Auditoria.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

   public static final String AUDITORIA_QUEUE = "auditoria.queue";
   public static final String AUDITORIA_EXCHANGE = "auditoria.exchange";
   public static final String AUDITORIA_ROUTING_KEY = "auditoria.routingkey";

   @Bean
   public Queue queue() {
      return new Queue(AUDITORIA_QUEUE, true);
   }

   @Bean
   public TopicExchange exchange() {
      return new TopicExchange(AUDITORIA_EXCHANGE);
   }

   @Bean
   public Binding binding(Queue queue, TopicExchange exchange) {
      return BindingBuilder.bind(queue).to(exchange).with(AUDITORIA_ROUTING_KEY);
   }
}
