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

### EventDriver
Register event handlers from listener class
```java
EventDriver driver = new EventDriver();

driver.register(new EventListener());
```

Add handler for event
```java
EventDriver driver = new EventDriver();

driver.on(MyEvent.class, () -> doSomething());

//Or multiple to the same event in one go
driver.on(MyEvent.class)
        .addHandler(() -> actionA())
        .addHandler(() -> actionB());
```

And to fire the event
```java
EventDriver driver = new EventDriver();

//TODO Add handlers to driver

//Fire event
driver.fire(new MyEvent());
```

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

### Stepper

This class can be used to "iterate" of sorts over a list without providing access to the list. Useful when you want to
do things in "steps" to distribute load over multiple threads for instance or multiple ticks

```java
List<Person> people;
Stepper<Person> stepper=new Stepper(people);

//A crude example, method doesn't actually exist
repeatingTask(()->{
    Person person=stepper.next();//Get the next in the list
    //Or previous with stepper.previous();
});
```

### Bucket

Spread data over multiple partitions to make it more digestible. It supports two partition strategies: `LOWEST_SIZE` for
even distribution, and `RANDOM` for random distribution. You can also implement your own by
using `IGenericPartitionStrategy` or `IPartitionStrategy`.

Example usage:

```java
//You can use either HASH(for HashSet) or CONCURRENT(for a concurrent Set), or implement your own with AbstractBucket or IBucket
//DefaultBucket is an enum with a factory for these basic implementations
IBucket<Person> bucket = DefaultBucket.HASH.newInstance(
        10, //Amount of partitions
        PartitionStrategies.LOWEST_SIZE //Partition strategy
);

//Use basic set modifications
bucket.add(person);
bucket.remove(person);

//A crude example, method doesn't actually exist
repeatingTask(() -> {
    //We can use the stepper to get a different partition per run
    IBucketPartition<Person> people = bucket.asStepper.next();
    for(Person person : people)
        //Do something with the entry
});
```

### Filter
Replacement for a set of `if return` statements.
```java
//Create a filter
Filter<Person> filter = new Filter<>()
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