# Camel

* Blog discussing the use of [aggregator](http://tmielke.blogspot.com/2009/01/using-camel-aggregator-correctly.html)
* [Comparison to Spring Integration](http://stackoverflow.com/questions/3034054/when-to-use-spring-integration-vs-camel)

# Basic info

## Message model

Two abstractions:

1. Message (`org.apache.camel.Message`)
2. Exchange ('org.apache.camel.Exchange')

### Message

Three parts:

* Headers
* Attachments
* Body

During routing, messages are contained in an exchange

### Exchange

An *exchange* in Camel is the message's container during routing. An exchange also provides support for the various types of interactions between systems, aka message exchange patterns (MEPs). MEPs are used to differentiate between one-way and request-response messaging styles.

* `InOnly` - a one-way message (aka an `Event` message)
* `InOut` - a request-response message

A Camel exchange has:

* ID (exchange ID)
* MEP
* Exception
* Properties - similar to message headers, but they last for the duration of the entire exchange. (read up a bit more)
* In message - input message, the in message contains the request message
* Out message - optional message that only exists if the MEP is `InOut`. The out message contains the reply message

## Overview

Camel is composed of:
* processors
* components
* routes 

All these are contained within the `CamelContext`

### CamelContext

One of the services that CamelContext provides is `Registry` which allows you to look up beans. Default will be a JNDI registry but using Camel from Spring, this will be the Spring `ApplicationContext`.

> Can the registry be based on Guice?

### Routes

Each route in Camel has a unique identifier (used for logging, debugging, monitoring and starting and stopping routes). Routes have exactly one input source for messages, effectively tied to an input endpoint.

### Processors

Handle things in between endpoints like
* EIPs
* Routing
* Transformation
* Mediation
* Enrichment
* Validation
* Interception

### Component

Associated with a name that's used in a URI and they act as a factory of endpoints.

E.g. a `FileComponent` is referred to by `file` in a URI, and it creates `FileEndpoint`s.

### Endpoint

An endpoint is the Camel abstraction that models the end of a channel through which a system can send or receive messages.

In Camel, you configure endpoints using URIs, such as `file:data/inbox?delay=5000`, and also refer to endpoints the same way.

Endpoint URIs are divided into three parts:

    file:data/inbox?delay=5000
    [1]:[2        ]?[3       ]

    scheme : context path ? options

Endpoint acts as a factory for creating consumers and producers that are capable of receiving and sending mesages to a particular endpoint.

### Producer

A producer is the Camel abstraction that refers to an entity capable of creating and sending a message to an endpoint.

When a message needs to be sent to an endpoint, the producer will create an exchange and populate it with data compatible with that particular endpoint. E.g. a `FileProducer` will write the message body to a file. A `JmsProducer`, will map the Camel message to a `javax.jms.Message` before sending it to a JMS destination.

### Consumer

A consumer is the service that receives messages produced by a producer, wraps them in an exchange and sends them to be processed. Consumers are the source of the exchanges being routed in Camel.

Two types of consumers:

* Event-driven
* Polling

#### Event-driven Consumer

Asynchronous receiver in the EIP world.

E.g. TCP/IP port, JMS queue

#### Polling Consumer

Synchronous receiver in EIP.

E.g. FTP server (?), File, email transports all use scheduled polling consumers.

### JMS Basics

In JMS, message consumers and producers talk to one another through an intermediary - JMS destination. A destination can be either a queue or a topic.

Queues are point-to-point, where each message has only one consumer. Topics operate on a publish/subscribe scheme; a single message may be delivered to many consumers if they have subscribed to the topic.

JMS also provides a `ConnectionFactory` that clients (like Camel) can use to create a connection with a JMS provider. JMS providers are usually referred to as brokers because they manage the communication between a message producer and a message consumer.

### How to configure Camel to use a JMS provider

Refer to chapter 2.2.2 in Camel in Action but in essence:

* Need a specific `ConnectionFactory` and configure Camel's JMS component with it

E.g. for ActiveMQ use `ActiveMQConnectionFactory` like

    ConnectionFactory cf = new ActiveMQConnectionFactory("vm://localhost");

* The `vm://localhost` URI means you should connect to an embedded broker named "localhost" running inside the current JVM. but in production, connect to a broker that's already running. and

* Use connection pooling for connecting to a JMS broker (see chapter 7)

### RouteBuilder in Java

    import org.apache.camel.builder.RouteBuilder;
    class MyRouteBuilder extends RouteBuilder {
      public void configure() throws Exception {
        ...
      }
    }

Then

    CamelContext context = new DefaultCamelContext();
    context.addRoutes(new MyRouteBuilder());

or

    CamelContext conext = new DefaultCamelContext();
    context.addRoutes(new RouteBuilder() {
      public void configure() throws Exception {
        ...
      }
    });

And within the `configure` method, define the routes using the Java DSL.

### TypeConverter

In Chapter 3.

### Processor

Use Processor to inspect an exchange in pipeline.

    from("ftp://...").process(new Processor() {
      public void process(Exchange exchange) throws Exception {
        System.out.println("Downloading " + exchange.getIn().getHeader("CanelFileName"));
      }
    }).to("jms:incomingOrders");

### Spring DSL

Refer to 2.4.2. Also note of maven goal `mvn camel:run`. Refer to [this](http://camel.apache.org/camel-run-maven-goal.html)

### Refer to advanced configuration options

When using the Spring CamelContext:

* Pluggable bean registries - chapter 4
* Tracer and delay mechanisms - chapter 12
* Custom class resolvers, tracing, fault handling and startup - chapter 13
* configuration of interceptors - chapter 6

### Routing and EIP

* Content-based router
* Message filters
* Multicasting
    * `from("jms:xmlOrders").multicast().to("jms:accounting", "jms:production");` - sequential
    * Parallel multicasting - `from("jms:xmlOrders").multicast().parallelProcessing().to("jms"accounting", "jms:production");`
* Using recipient lists

### Way to inspect messages as they're flowing through the route

Rather than waiting until they reach the end. Use wire tap.

### Using the wireTap method

Instead of using a processor to inspect incoming message, use wire tap EIP.

A wire tap is a fixed recipient list that sends a copy of message traveling from a source to a destination to a secondary destination.

    from("jms:incomingOrders")
    .wireTap("jms:orderAudit")
    .choice()
      .when(header("CamelFileName").endsWith(".xml"))
        .to("jms:xmlOrders")
      .when(header("CamelFileName").regex("^.*(csv|csl)$"))
        .to("jms:csvOrders")
      .otherwise()
        .to("jms:badOrders");

The code sends a copy of the exchange to the orderAudit queue, and the original exchange continues on through the route. Camel doesn't wait for a response from the wire tap because the wire tap sets the message exchange pattern (MEP) to `InOnly`. This means that the message will be sent to the `orderAudit` queue in a fire-and-forget fashion - it won't wait for a reply.

> Wire tap is useful monitoring tool but Camel provides more powerful tracing and auditing tools - chapter 12.

## Data Transformation

Definitions - `data transformation` covers two types of transformation:

* Data format transformation - the data format of the message body is transformed from one form to another. E.g. CSV record is formatted as XML
* Data type transformation - the data type of the message body is transformed from one type to another. E.g. `java.lang.String` is transformed into a `javax.jms.TextMessage`.

### Using the content enricher EIP

In the content enricher EIP, an existing message has data added to it from another source.

Two type:

* Poll enricher (`pollEnrich`)
* Enrich (use it for enriching the current message with the reply from a web service call.)

Need to know about `AggregationStrategy` and need to implement `aggregate` method to be used with enrichment.

### Transforming XML with object marshaling

* Using XStream
    * marshal / unmarshal - quite simple, need to include xstream.jar
* Using JAXB
    * need to do more work, need to annotate Java

## In-memory messaging

Three components:

* Direct
* SEDA (staged event-driven architecture)
* VM

The `direct` component is synchronous and both `SEDA` and `VM` are asynchronous. `SEDA` can be used for communication within a single `CamelContext` whereas `VM` component can be used for communication within a JVM, hence if you have two `CamelContext`s, you can send messages between them using the VM component.

