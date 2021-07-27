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
There's also a manager class that acts as both an entry and service. `T` being the identifier type and `R` being the identifier type of MyEntry
```java
public class MyManager extends DataManager<T, R, MyEntry> {
    
    public MyManager(T id) {
        super(id);
    }
    
}
```