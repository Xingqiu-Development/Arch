# Arch
Arch is a general core plugin for [Spigot](https://www.spigotmc.org/)

# Developer Guide
* Use finals whenever possible (not required for parameters)
  * Finals reduce file size & improve performance. This can be seen when decompiling the JAR to bytecode.
* Document your functions/methods and constructors. 

# Data Handling
We're using our own database handler primarily focused on flexibility and to reduce redundant code.
   
Objects implementing Loadable can be serialized and deserialized by our database handler, and these objects can have objects called Data inside of  them.

Example:

Loadable implementation:
```java
public class ExampleLoadable implements Loadable<Data> {
    
    private UUID uuid;
    private List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

   public void setData(List<Data> list) {
        this.data = list;
   }
   
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
   
    public void getUuid() {
        return this.uuid;
    }   
}
```

Data implementation:

```java
public class ExampleData implements Data { }
```

You can easily add data to the Loadable object using ``Loadable#addData``
```java
final ExampleLoadable loadable = new ExampleLoadable();
loadable.addData(new ExampleData());
```

Data objects can easily be found using the ``Loadable#findData`` method
```java
final ExampleLoadable loadable = new ExampleLoadable();
loadable.findData(ExampleData.class);
```