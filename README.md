# Builder
Annotation Processor for the Builder Pattern

## Usage

### Add annotations to fields manually
```java
@Builder
class Manual {
  @BuilderField
  private int var1;
  
  @BuilderField(required = true)
  private String var2;
  
  private Object var3; // Not included in the builder
  
  public int getVar1() { return var1; }
  public void setVar1(int var1) { this.var1 = var1; }
  
  public String getVar2() { return var1; }
  public void setVar2(String var2) { this.var2 = var2; }
  
  public Object getVar3() { return var3; }
}
```

### Add annotations to fields automatically
```java
@Builder(all = true)
class Automatic {
  private int var1;
  
  @BuilderField(required = true)
  private String var2;
  
  @NotNull // Has the equivelant effect of using: @BuilderField(required = true)
  private Object var3;
  
  public int getVar1() { return var1; }
  public void setVar1(int var1) { this.var1 = var1; }
  
  // Setter not needed for required fields
  public String getVar2() { return var2; }
  
  public Object getVar3() { return var3; }
}
```
