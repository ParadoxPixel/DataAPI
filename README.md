# DataAPI

### DataInitializer
Getting a new instance without parameters
```java
MyClass instance = DataInitializer.newInstance(MyClass.class);
```

Getting a new instance with a map
```java
MyClass instance = DataInitializer.newInstance(MyClass.class, new HashMap<String, Object>() {{
    put("field_name", value);
}});
```

Getting a new instance with an array of objects
```java
Object[] objects;
MyClass instance = DataInitializer.newInstance(MyClass.class, objects);

//Or just give it multiple parameters
MyClass instance = DataInitializer.newInstance(MyClass.class, "String #1", "String #2");
```

Calling a static method. Return value is automatically casted while void always returns null
```java
//Without parameters
T value = DataInitializer.callMethod(MyClass.class, "method_name");

//With parameters
T value = DataInitializer.callMethod(MyClass.class, "method_name", "Parameter #1", 2);
```

Calling a non-static method. Return value is automatically casted while void always returns null
```java
MyClass instance;

//Without parameters
T value = DataInitializer.callMethod(instance, "method_name");

//With parameters
T value = DataInitializer.callMethod(instance, "method_name", "Parameter #1", 2);
```
---
### DataRegistry
```java
//Get new registry for base class
DataRegistry<ClassToExtend> registry = new DataRegistry<>();

registry.addHandler(ClassToExtend::onEnable); //Consumer that get's called on register
        
//Register class
registry.register(ClassThatExtends.class);

//Get instance from registered class
ClassThatExtends instance = registry.get(ClassThatExtends.class);

//Or just register and get instance
ClassThatExtends instance = registry.register(ClassThatExtends.class);
```
---
### DataInjector
```java
DataInjector dataInjector = new DataInjector();

//Use it with an Object
dataInjector.register(String.class, () -> "Some value");

//Use it with an Interface
dataInjector.register(IEvent.class, clazz -> registry.get(clazz));

//Injection from class
MyClass instance = dataInjector.inject(MyClass.class);

//Injection from object
dataInjector.inject(instance);
```
---
### EventDriver
Register event handlers from listener class
```java
EventDriver driver = new EventDriver();

driver.register(new EventListener());
```

Add handler for event
```java
EventDriver driver = new EventDriver();

driver.on(MyEvent.class, e -> doSomething());

//Or to add filters to handler
driver.on(MyEvent.class)
        .filter(e -> e.getValue() != null)
        .filter(e -> e.getAnswer() == 42)
        .handler(e -> actionB());
```

And to fire the event
```java
EventDriver driver = new EventDriver();

//TODO Add handlers to driver

//Fire event
driver.fire(new MyEvent());
```
---
### DataService
Create an entry class
```java
public class MyEntry extends Data<T> {
    
    public MyEntry(T id) {
        super(id);
    }
    
}
```
Create service class
```java
public class MyService extends DataService<T, MyEntry> {
    
}
```
You can now use the service
```java
MyService service = new MyService();

//Add an MyEntry instance
service.add(entry);

//Check if has id of type T
service.has(id);

//Get entry of type T
MyEntry entry = service.get(id);

//Or get option of entry
Optional<MyEntry> option = service.getOptional(id);

//Remove entry with id and return value
MyEntry value = service.remove(id);
```

There's also a manager class that acts as both an entry and service. `T` being the identifier type and `R` being the
identifier type of MyEntry

```java
public class MyManager extends DataManager<T, R, MyEntry> {

    public MyManager(T id) {
        super(id);
    }

}
```
---
### Stepper

This class can be used to "iterate" of sorts over a list without providing access to the list. Useful when you want to
do things in "steps" to distribute load over multiple threads for instance or multiple ticks

```java
List<Person> people;
Stepper<Person> stepper=new Stepper(people);

//A crude example, method doesn't actually exist
repeatingTask(() -> {
    Person person = stepper.next();//Get the next in the list
    //Or previous with stepper.previous();
});
```
---
### Bucket

Spread data over multiple partitions to make it more digestible. It supports two partition strategies: `LOWEST_SIZE` for
even distribution, and `RANDOM` for random distribution. You can also implement your own by
using `IGeneric[Set,Map]PartitionStrategy` or `I[Set,Map]PartitionStrategy`.

Example usage for set:
```java
//You can use either HASH(for HashSet) or CONCURRENT(for a concurrent Set), or implement your own with AbstractSetBucket or ISetBucket
//DefaultSetBucket is an enum with a factory for these basic implementations
ISetBucket<Person> bucket = DefaultSetBucket.HASH.newInstance(
        10, //Amount of partitions
        SetPartitionStrategies.LOWEST_SIZE //Partition strategy
);

//Use basic set modifications
bucket.add(person);
bucket.remove(person);

//A crude example, method doesn't actually exist
repeatingTask(() -> {
    //We can use the stepper to get a different partition per run
    ISetBucketPartition<Person> people = bucket.asStepper.next();
    for(Person person : people)
        //Do something with the entry
});
```

Example usage for map:
```java
//You can use either HASH(for HashMap) or CONCURRENT(for ConcurrentHashMap), or implement your own with AbstractMapBucket or IMapBucket
//DefaultMapBucket is an enum with a factory for these basic implementations
IMapBucket<Integer, Person> bucket = DefaultMapBucket.HASH.newInstance(
        10, //Amount of partitions
        MapPartitionStrategies.LOWEST_SIZE //Partition strategy
);

//Use basic set modifications
bucket.put(person_id, person);
bucket.remove(person_id);

//A crude example, method doesn't actually exist
repeatingTask(() -> {
    //We can use the stepper to get a different partition per run
    IMapBucketPartition<Integer, Person> people = bucket.asStepper.next();
    for(Map.Entry<Integer, Person> entry : people.entrySet())
        //Do something with the entry
});
```
---
### Filter
Replacement for a set of `if return` statements.
```java
//Create a filter
Filter<Person> filter = Filter.of(Person.class);
filter.add(p -> p != null)
        .add(p -> p.getAge() >= 18);

//Or inline
Filter<Person> filter = Filter.of(Person.class)
        .add(p -> p != null)
        .add(p -> p.getAge() >= 18);

//Use the filter
if(filter.check(person))
    //Do something with valid person
    
//Or use it with a consumer
filter.check(person, result -> {
    if(result){
        //Passed
    } else{
        //Failed
    }
})
```
We can also add checks from classes or objects
```java
public class MyFilters {

    @FilterCheck
    public boolean checkAge(Person person) {
        return person.getAge() >= 18;
    }

    @FilterCheck
    public boolean notNull(Person person) {
        return person != null;
    }
}

//Load it from clazz
filter.add(MyFilters.class);

//Or from instance
filter.add(new MyFilters());
```
---
### NamespaceMap
Can be used to store values at a path and retrieve it with wildcards
```java
NamespaceMap<MyClass> map = new NamespaceMap();
map.set("my.path.to.value", new MyClass());

//Be specific for a single value
MyClass values = map.first("my.path.to.value");

//Or use a wildcard for multiple
Set<MyClass> values = map.get("my.path.to.*");//Will match anything that is the same length, and starts with `my.path.to`

//You can also use it like so and combine these
map.get("my.path.to.v*"); //Wildcard at end
map.get("my.path.to.*e"); //Wildcard at start
map.get("my.path.to.v*e"); //Wildcard in between

//You can also use a double wildcard to ignore the rest of the path
map.get("my.path.**"); //Will match any path starting with `my.path`
```
---
### MessageBroker
Messaging channels with namespace path's
```java
MessageBroker broker = new MessageBroker();

//Get will get the channel or make a new one if it doesn't exist
MessageChannel<MyMessage> channel = broker.get(
        "my.message.path",
        MyMessage.class
);

//Channel is null if the broker is unable to get and create the channel
if(channel == null)
    return;

ChannelAgent<MyMessage> agent = channel.newAgent();

//You can add multiple listeners to the same agent
agent.listen(msg -> {
    //Do something with the message
});

//Send message to path(works the same as `NamespaceMap.get`)
broker.send(
        new MyMessage(),
        "my.message.path"
);

//Or multiple paths, it'll only send to the channels that can handle the message type
broker.send(
        new MyMessage(),
        "my.message.path",
        "my.message.data.*"
);
```