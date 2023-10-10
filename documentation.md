
## Installing and Running docker
```
docker pull rmohr/activemq
docker run -p 61616:61616 -p 8161:8161 rmohr/activemq
```

## Viewing the topics on the ActiveMQ Website

localhost:8161
Click on the link on the page and login with admin admin

## Files

`build.gradle:`

```
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-activemq'
implementation 'com.fasterxml.jackson.core:jackson-databind:2.12.5'
```

```
bootRun {
//	if (project.hasProperty('args')) {
//		args project.args.split(',')
//	}
	args = ["war1", "8081"] //war1 or war2 or war... or main and the port for spring boot
}
```

`Warehouse2Application.java`

```
@SpringBootApplication
public class Warehouse2Application {

	public static Sender sender;
	public static Receiver receiver;

	public static void main(String[] args) throws InterruptedException {
		SpringApplication app = new SpringApplication(Warehouse2Application.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", Integer.valueOf(args[1])));
		app.run(args);

		String topic = args[0];

		sender = new Sender(topic);

		if(topic.equals("main")) {
			receiver = new Receiver("war1");
		}
	}
}
```

`Sender.java`

```
ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, password, url );
connection = connectionFactory.createConnection();
connection.start();

// Create the session
session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
destination = session.createTopic( this.topic );

// Create the producer.
producer = session.createProducer(destination);
producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
```

```
public void sendMessageToTopic(String message) {
    try {
        TextMessage textMessage = session.createTextMessage(message);
        producer.send(textMessage);

    } catch (JMSException e) {
        throw new RuntimeException(e);
    }
}
```

`Receiver.java`

```
// Create the session
session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
destination = session.createTopic( this.topic );

consumer = session.createConsumer(destination);
consumer.setMessageListener(new LocalListener());
```

`LocalListener()`

```
public class LocalListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Received " + ((TextMessage) message).getText());
            ((TextMessage) message).acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

`WarehouseController.java`

```
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value="/warehouse/{inID}/data", produces = MediaType.APPLICATION_JSON_VALUE)
public WarehouseData warehouseData(@PathVariable String inID ) {
    WarehouseData data = service.getWarehouseData( inID );

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = null;
    try {
        jsonString = objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }

    Warehouse2Application.sender.sendMessageToTopic(jsonString);
    return data;
}
```
