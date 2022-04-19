> Mockito is about focusing on interactions between objects, which is the most essential part of *Object Oriented Programming*

​																																									——鲁迅

#### Introduction to Unit Testing

A unit test is a test related to a single responsibility of a single class, often referred to as the System Under Test (SUT). The purpose of unit tests is to verify that the code in an SUT works

mocito 帮你 mock 一个对象，可以记录你和此对象的交互。



#### 开始



**核心用法**

这个 when 里面是个方法调用，虽然很不可思议但就是这么用的：

```java
when(someObject.someMethod()).thenReturn(someValue);
```

void 方法不一样：

```java
doNothing().when(someObject).someVoidMethod();
```



`mock`一个类返回一个对象，叫做 mock；`when...then...`叫 stub。

stub 可以理解为篡改一个方法的返回值；可以被覆盖（多次 stub），但不推荐。

stub 的方法不需要被 verify！



两种方法注入 mock 对象

```java
Flower flowerMock=Mockito.mock(Flower.class);
```

或

```java
// 想使用这种方式必须
// a) MockitoAnnotations.initMocks( testClass ) 
// 或 b) 测试类加注解 @RunWith(MockitoJUnit4Runner.class)
@Mock
private Flower flowerMock;
```



#### Argument matchers

stub 某个方法时可能用到，参考 `ArgumentMatcher` doc 看更多信息。

> Be reasonable with using complicated argument matching. The natural matching style using `equals()` with occasional `anyX()` matchers tend to give clean & simple tests. Sometimes it's just better to refactor the code to allow `equals()` matching or even implement `equals()` method to help out with testing

一般 `anyXXX()` 或使用 `equals()` 足够了

```java
//stubbing using built-in anyInt() argument matcher
 when(mockedList.get(anyInt())).thenReturn("element");

 //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
 when(mockedList.contains(argThat(isValid()))).thenReturn(true);

 //following prints "element"
 System.out.println(mockedList.get(999));

 //you can also verify using an argument matcher
 verify(mockedList).get(anyInt());

 //argument matchers can also be written as Java 8 Lambdas
 verify(mockedList).add(argThat(someString -> someString.length() > 5));
```



老生常谈：某个方法所有参数，要么全用 matcher，要么不用

*If you are using argument matchers, **all arguments** have to be provided by matchers*

```java
  verify(mock).someMethod(anyInt(), anyString(), eq("third argument"));
   //above is correct - eq() is also an argument matcher

   verify(mock).someMethod(anyInt(), anyString(), "third argument");
   //above is incorrect - exception will be thrown because third argument is given without an argument matcher.
```



> Matcher methods like `any()`, `eq()` **do not** return matchers. Internally, they record a matcher on a stack and return a dummy value (usually null). This implementation is due to static type safety imposed by the java compiler. The consequence is that you cannot use `any()`, `eq()` methods outside of verified/stubbed method



#### Stubbing Method's Returned Value

> One of the basic functions of mocking frameworks is an ability to return a given value when a specific method is called. It can be done using `Mockito.when()` in conjunction with` thenReturn ()` . This process of defining how a given mock method should behave is called stubbing.



来个例子：

```java
	import org.testng.annotations.Test;
    import static org.mockito.Mockito.mock;
    import static org.mockito.Mockito.when;
    import static org.testng.Assert.assertEquals;

    public class SimpleStubbingTest {
        public static final int TEST_NUMBER_OF_LEAFS = 5;

        @Test
        public void shouldReturnGivenValue() {
            Flower flowerMock = mock(Flower.class);
            when(flowerMock.getNumberOfLeafs()).thenReturn(TEST_NUMBER_OF_LEAFS);

            int numberOfLeafs = flowerMock.getNumberOfLeafs();

            assertEquals(numberOfLeafs, TEST_NUMBER_OF_LEAFS);
        }
    }
```



> Following an arrange-act-assert pattern (similar to given- when-then, from Behavior Driven Development) a test should be split into three parts (blocks), each with a specified responsibility

BDD 风格的 mockito api，其实就是个别名，实现是一样的：

```java
	import static org.mockito.BDDMockito.given;
    import static org.mockito.Mockito.mock;
    import static org.testng.Assert.assertEquals;

    @Test
    public void shouldReturnGivenValueUsingBDDSemantics() {
    	//given
        Flower flowerMock = mock(Flower.class);
        given(flowerMock.getNumberOfLeafs()).willReturn(TEST_NUMBER_OF_LEAFS);

        //when
        int numberOfLeafs = flowerMock.getNumberOfLeafs();

        //then
        assertEquals(numberOfLeafs, TEST_NUMBER_OF_LEAFS);
    }
```



没调用过的参数，默认返回 default value：

```java
	@Test
    public void shouldMatchSimpleArgument() {
        WateringScheduler schedulerMock = mock(WateringScheduler.class);    
        
        given(schedulerMock.getNumberOfPlantsScheduledOnDate(WANTED_DATE))
            .willReturn(VALUE_FOR_WANTED_ARGUMENT);

        int numberForWantedArgument = schedulerMock
            						  .getNumberOfPlantsScheduledOnDate(WANTED_DATE);
        int numberForAnyOtherArgument = schedulerMock
            						  .getNumberOfPlantsScheduledOnDate(ANY_OTHER_DATE);

        assertEquals(numberForWantedArgument, VALUE_FOR_WANTED_ARGUMENT);
        assertEquals(numberForAnyOtherArgument, 0);  //default value for int
    }
```



匹配方法时可以使用 matcher

> If an argument matcher is used for at least one argument, all arguments must be provided by matchers，方法参数要么都是 matcher，要么没有 matcher，不然会报错

```java
given(plantSearcherMock.smellyMethod(anyInt(), contains("asparag"),eq("red")))
    .willReturn(true);
    //given(plantSearcherMock.smellyMethod(anyInt(), contains("asparag"), "red")).willReturn(true);
    //incorrect - would throw an exception
```

会报下面的温馨提示：

```java
org.mockito.exceptions.misusing.InvalidUseOfMatchersException:
    Invalid use of argument matchers!
    3 matchers expected, 2 recorded.
    This exception may occur if matchers are combined with raw values:
    //incorrect:
    someMethod(anyObject(), "raw String");
    When using matchers, all arguments have to be provided by matchers.
    For example:
    //correct:
    someMethod(anyObject(), eq("String by matcher"));

    For more info see javadoc for Matchers class.
```



自定义一个 matcher 用来匹配方法参数：

> It is also possible to create a custom matcher by extending the `ArgumentMatcher` class and using it together with `argThat ()`

```java
	given(schedulerMock.getNumberOfPlantsScheduledOnDate(
    	argThat(haveHourFieldEqualTo(7)))).willReturn(1);

    //with the util method to create a matcher
    private ArgumentMatcher haveHourFieldEqualTo(final int hour) {
        return new ArgumentMatcher() {
            @Override
            public boolean matches(Object argument) {
            	return ((Date) argument).getHours() == hour;
            }
        };
    }
```



#### stub 连续调用

可以让同一方法不同调用返回不同值，最后一个值以后的调用都返回这个值：

```java
	@Test
    public void shouldReturnLastDefinedValueConsistently() {
        WaterSource waterSource = mock(WaterSource.class);
        given(waterSource.getWaterPressure()).willReturn(3, 5);

        assertEquals(waterSource.getWaterPressure(), 3);
        assertEquals(waterSource.getWaterPressure(), 5);
        // 注意这里
        assertEquals(waterSource.getWaterPressure(), 5);
    }
```



```java
 when(mock.someMethod("some arg"))
   .thenThrow(new RuntimeException())
   .thenReturn("foo");

 //First call: throws runtime exception:
 mock.someMethod("some arg");

 //Second call: prints "foo"
 System.out.println(mock.someMethod("some arg"));

 //Any consecutive call: prints "foo" as well (last stubbing wins).
 System.out.println(mock.someMethod("some arg"));
```



#### stub void 方法

使用 `doReturn()|doThrow()| doAnswer()|doNothing()|doCallRealMethod() family of methods`

它们的使用场景：

- stub void methods
- stub methods on spy objects (see below)
- stub the same method more than once, to change the behaviour of a mock in the middle of a test

对于没有返回值的方法，不能使用 `given`..，而应该用 `willXXX, doXXX`：

```java
	@Test(expectedExceptions = WaterException.class)
    public void shouldStubVoidMethod() {
        WaterSource waterSourceMock = mock(WaterSource.class);
        doThrow(WaterException.class).when(waterSourceMock).doSelfCheck();
        //the same with BDD semantics
        //willThrow(WaterException.class).given(waterSourceMock).doSelfCheck();

        waterSourceMock.doSelfCheck();
        //exception expected
    }
```

这两个方法推荐只在返回值为 void 的方法上使用，不然只能在运行时发现类型错误：

> Will/doReturn does not use that trick. Will/doReturn can be also used for stubbing non-void methods, though it is not recommended because it cannot detect wrong return method types at a compilation time (only an exception at runtime will be thrown)



#### 使用 answer 接口

> **Warning:** The need to use a custom answer may indicate that tested code is too complicated and should be re-factored

```java
when(mock.someMethod(anyString())).thenAnswer(
     new Answer() {
         public Object answer(InvocationOnMock invocation) {
             Object[] args = invocation.getArguments();
             Object mock = invocation.getMock();
             return "called with arguments: " + Arrays.toString(args);
         }
 });

 //Following prints "called with arguments: [foo]"
 System.out.println(mock.someMethod("foo"));
```



>  Mockito contains a generic Answer interface allowing the implementation of a callback method and providing access to invocation parameters (used arguments, a called method, and a mock instance)

先告诉你如果用这个接口，说明测试方法太复杂了需要被重构，但还是告诉你怎么用这个接口：

```java
	@Test
    public void shouldReturnTheSameValue() {
        FlowerFilter filterMock = mock(FlowerFilter.class);
        given(filterMock.filterNoOfFlowers(anyInt())).will(returnFirstArgument());

        int filteredNoOfFlowers = filterMock.filterNoOfFlowers(TEST_NUMBER_OF_FLOWERS);

        assertEquals(filteredNoOfFlowers, TEST_NUMBER_OF_FLOWERS);
    }

    //reusable answer class
    public class ReturnFirstArgument implements Answer<Object> {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Object[] arguments = invocation.getArguments();
            if (arguments.length == 0) {
            	throw new MockitoException("...");
       		 }
            return arguments[0];
        }
        
        public static ReturnFirstArgument returnFirstArgument() {
        	return new ReturnFirstArgument();
        }
    }
```

使用 lambda 更简洁，但判断参数时需要更多强转：

```java
 // answer by returning 12 every time
 doAnswer(invocation -> 12).when(mock).doSomething();

 // answer by using one of the parameters - converting into the right
 // type as your go - in this case, returning the length of the second string parameter
 // as the answer. This gets long-winded quickly, with casting of parameters.
 doAnswer(invocation -> ((String)invocation.getArgument(1)).length())
     .when(mock).doSomething(anyString(), anyString(), anyString());
 
```

下面是一个详尽的例子，各种风格的自定义 answer：

```java
 // Example interface to be mocked has a function like:
 void execute(String operand, Callback callback);

 // the example callback has a function and the class under test
 // will depend on the callback being invoked
 void receive(String item);

 // Java 8 - style 1
 doAnswer(AdditionalAnswers.answerVoid((operand, callback) -> callback.receive("dummy"))
     .when(mock).execute(anyString(), any(Callback.class));

 // Java 8 - style 2 - assuming static import of AdditionalAnswers
 doAnswer(answerVoid((String operand, Callback callback) -> callback.receive("dummy"))
     .when(mock).execute(anyString(), any(Callback.class));

 // Java 8 - style 3 - where mocking function to is a static member of test class
 private static void dummyCallbackImpl(String operation, Callback callback) {
     callback.receive("dummy");
 }

 doAnswer(answerVoid(TestClass::dummyCallbackImpl)
     .when(mock).execute(anyString(), any(Callback.class));

 // Java 7
 doAnswer(answerVoid(new VoidAnswer2() {
     public void answer(String operation, Callback callback) {
         callback.receive("dummy");
     }})).when(mock).execute(anyString(), any(Callback.class));

 // returning a value is possible with the answer() function
 // and the non-void version of the functional interfaces
 // so if the mock interface had a method like
 boolean isSameString(String input1, String input2);

 // this could be mocked
 // Java 8
 doAnswer(AdditionalAnswers.answer((input1, input2) -> input1.equals(input2))))
     .when(mock).execute(anyString(), anyString());

 // Java 7
 doAnswer(answer(new Answer2() {
     public String answer(String input1, String input2) {
         return input1 + input2;
     }})).when(mock).execute(anyString(), anyString());
```

上面这种风格不需要强转参数，AdditionalAnswers 提供了一个 answer 方法，接收至多五个参数的 answer lambda，很方便。但不帮你检查类型！可能会跑出运行时异常。

#### Verifying Behavior

> Once created, a mock remembers all operations performed on it

```java
	WaterSourcewaterSourceMock=mock(WaterSource.class);
    waterSourceMock.doSelfCheck();

    verify(waterSourceMock).doSelfCheck();
```

它可以验证调了多少次，至多，至少，没调，设置一个超时时间等。

```java
	verify(waterSourceMock, never()).doSelfCheck();
    verify(waterSourceMock, times(2)).getWaterPressure();
    verify(waterSourceMock, atLeast(1)).getWaterTemperature();
```

> As an alternative to never (), which works only for the specified call, `verifyZeroInteractions ( Object ... mocks)` method can be used to verify no interaction with any method of the given mock(s)



**Finding redundant invocations** 

> Additionally, there is one more method available, called 
>
> ​	`verifyNoMoreInteractions ( Object ... mocks)`,
>
>  which allows to ensure that no more interaction (except the already verified ones) was performed with the mock(s)



> A word of **warning**: Some users who did a lot of classic, expect-run-verify mocking tend to use `verifyNoMoreInteractions()` very often, even in every test method. `verifyNoMoreInteractions()` is not recommended to use in every test method. `verifyNoMoreInteractions()` is a handy assertion from the interaction testing toolkit. Use it only when it's relevant. Abusing it leads to **overspecified**, **less maintainable** tests



> Mockito does not automatically verify calls.



#### Custom verification failure message 

```java
 // will print a custom message on verification failure
 verify(mock, description("This will print on failure")).someMethod();

 // will work with any verification mode
 verify(mock, times(2).description("someMethod should be called twice")).someMethod();
```



#### Verifying Call Order

> Verification in order is flexible - **you don't have to verify all interactions** one-by-one but only those that you are interested in testing in order

看看你是不是按顺序调用的方法：

> **Warning:** The need to use a custom answer may indicate that tested code is too complicated and should be re-factored.

```java
	@Test
    publicvoidshouldVerifyInOrderThroughDifferentMocks(){
        WaterSourcewaterSource1=mock(WaterSource.class);
        WaterSourcewaterSource2=mock(WaterSource.class);

        waterSource1.doSelfCheck();
        waterSource2.getWaterPressure();
        waterSource1.getWaterTemperature();

        InOrder inOrder=inOrder(waterSource1,waterSource2);
        inOrder.verify(waterSource1).doSelfCheck();
        inOrder.verify(waterSource2).getWaterPressure();
        inOrder.verify(waterSource1).getWaterTemperature();
    }
```

得严格按顺序验证，对象间，对象内；不然就报错。



#### Verifying with Argument Matching

> Mockito offers an `ArgumentCaptor` class, enabling us to retrieve the argument passed to a mock

```java
	// 在这用真实参数调了一下 mock 对象
	//when
    flowerSearcherMock.findMatching(searchCriteria);

    //then
	// 这应该使用装饰器，包了一个 SearchCriteria
    ArgumentCaptor<SearchCriteria> captor = ArgumentCaptor
    									 .forClass(SearchCriteria.class);
	// 在这使用 ArgumentCaptor 验证 mock 对象接收到的参数
    Verify(flowerSearcherMock).findMatching(captor.capture());
    SearchCriteria usedSearchCriteria = captor.getValue();
    assertEquals(usedSearchCriteria.getColor(), "yellow");
    assertEquals(usedSearchCriteria.getNumberOfBuds(), 3)
```



##### 不理解：`captor.getAllValues()` 报错，只能获取一次的值



##### Java 8 Lambda Matcher Support 

如果要验证方法调用，使用的参数是否符合预期，可以用 `ArgumentCaptor`，也可以用下面的方式：

```java
 // verify a list only had strings of a certain length added to it
 // note - this will only compile under Java 8
 verify(list, times(2)).add(argThat(string -> string.length() < 5));

 // Java 7 equivalent - not as neat
 verify(list, times(2)).add(argThat(new ArgumentMatcher(){
     public boolean matches(String arg) {
         return arg.length() < 5;
     }
 }));

 // more complex Java 8 example - where you can specify complex verification behaviour functionally
 verify(target, times(1)).receiveComplexObject(argThat(obj -> obj.getSubObject().get(0).equals("expected")));

 // this can also be used when defining the behaviour of a mock under different inputs
 // in this case if the input list was fewer than 3 items the mock returns null
 when(mock.someMethod(argThat(list -> list.size()<3))).thenReturn(null);
```

可以看到，argThat 不仅可以用在 verify 阶段，还可以用在 stub 阶段。



#### Verifying with Timeout

*Allows verifying with timeout. It causes a verify to wait for a specified period of time for a desired interaction rather than fails immediately if had not already happened. May be useful for testing in concurrent conditions*



等一段时间之后再验证对象

> **Warning:** Currently, verifying with timeout doesn't work with inOrder verification.

```java
	@Test
    publicvoidshouldFailForLateCall(){
        WaterSourcewaterSourceMock = mock(WaterSource.class);
        Thread t = waitAndCallSelfCheck(40, waterSourceMock);

        t.start();

        verify(waterSourceMock,never()).doSelfCheck();
        try{
            verify(waterSourceMock, timeout(20)).doSelfCheck();
            fail("verificationshouldfail");
        }catch(MockitoAssertionErrore){
        	//expected
    	}
    }

```



#### Spying on Real Objects

仅用于部分 mock 旧代码，新代码不需要用 spy。

The spy calls **real** methods unless they are stubbed.

Real spies should be used **carefully and occasionally**, for example when dealing with legacy code.

Spying on real objects can be associated with "partial mocking" concept.

你 stub 了，它返回 stub 之后的值；没 stub，调用真实方法。

```java
   List list = new LinkedList();
   List spy = spy(list);

   //optionally, you can stub out some methods:
   when(spy.size()).thenReturn(100);

   //using the spy calls *real* methods
   spy.add("one");
   spy.add("two");

   //prints "one" - the first element of a list
   System.out.println(spy.get(0));

   //size() method was stubbed - 100 is printed
   System.out.println(spy.size());

   //optionally, you can verify
   verify(spy).add("one");
   verify(spy).add("two");
```



> With Mockito, you can use real objects instead of mocks by replacing only some of their methods with the stubbed ones. Usually there is no reason to spy on real objects, and it can be a sign of a code smell, but in some situations (like working with legacy code and IoC containers) it allows us to test things impossible to test with pure mocks

看个例子：

```java
	@Test
    publicvoidshouldStubMethodAndCallRealNotStubbedMethod(){
        Flower realFlower = newFlower();
        realFlower.setNumberOfLeafs(ORIGINAL_NUMBER_OF_LEAFS);
        Flower flowerSpy=spy(realFlower);
        willDoNothing().given(flowerSpy).setNumberOfLeafs(anyInt());

        flowerSpy.setNumberOfLeafs(NEW_NUMBER_OF_LEAFS);//stubbed‚àí should donothing

        verify(flowerSpy).setNumberOfLeafs(NEW_NUMBER_OF_LEAFS);
        //value was not changed
        assertEquals(flowerSpy.getNumberOfLeafs(), ORIGINAL_NUMBER_OF_LEAFS);
    }
```

什么意思呢？有一个实际对象 realFlower，spy 它返回一个 spy，然后对这个 spy 做一些操作，然后验证这个 spy，我们的工作就做完了。与此同时，原始对象 realFlower 不受影响。

> When working with spies it is required to use the `willXXX..given/ doXXX..when` methods family instead of `given .. willXXX/when.. thenXXX`. This prevents unnecessary calls to a real method during stubbing

来看看原因：

```java
   List list = new LinkedList();
   List spy = spy(list);

   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
   when(spy.get(0)).thenReturn("foo");

   //You have to use doReturn() for stubbing
   doReturn("foo").when(spy).get(0);
```

***此外，不要 stub final method！***



##### spy 抽象类

可以指定构造、（非静态）内部类：

```java
//convenience API, new overloaded spy() method:
 SomeAbstract spy = spy(SomeAbstract.class);

 //Mocking abstract methods, spying default methods of an interface (only available since 2.7.13)
 Function function = spy(Function.class);

 //Robust API, via settings builder:
 OtherAbstract spy = mock(OtherAbstract.class, withSettings()
    .useConstructor().defaultAnswer(CALLS_REAL_METHODS));

 //Mocking an abstract class with constructor arguments (only available since 2.7.14)
 SomeAbstract spy = mock(SomeAbstract.class, withSettings()
   .useConstructor("arg1", 123).defaultAnswer(CALLS_REAL_METHODS));

 //Mocking a non-static inner abstract class:
 InnerAbstract spy = mock(InnerAbstract.class, withSettings()
    .useConstructor().outerInstance(outerInstance).defaultAnswer(CALLS_REAL_METHODS));
```





#### Annotations

> To get annotations to function, you need to either call `MockitoAnnotations.initMocks(testClass)`
>
> (usually in a @Before method ) or use MockitoJUnit4Runner as a JUnit runner.

注入，和 spring 有点像！

```java
	//with constructor: PlantWaterer(WaterSource waterSource,
    // WateringScheduler wateringScheduler) {...}

    public class MockInjectingTest {
        // 自动帮你 mock 一个对象
        @Mock
        private WaterSource waterSourceMock;

        // 可以 spy 一个已存在的对象
        @Spy
        private WateringScheduler wateringSchedulerSpy;

        // 这个东西可以注入 field，setter 和 构造
        @InjectMocks
        private PlantWaterer plantWaterer;

        // 这样或类上方 @RunWith(MockitoJUnit4Runner.class)
        @BeforeMethod
        public void init() {
        	MockitoAnnotations.initMocks(this);
        }

        // 这个例子说明 @InjectMocks 注入了一个 mock 和一个 spy
        @Test
        public void shouldInjectMocks() {
            assertNotNull(plantWaterer.getWaterSource());
            assertNotNull(plantWaterer.getWateringScheduler());
        }
    }
```



#### Changing the Mock Default Return Value

mock 方法（或 @Mock(answer=xxxx))有可选的 answer：

```
RETURNS_DEFAULTS 默认
RETURNS_SMART_NULLS 返回一个间谍

// 作者说后三个 answer，良好的测试用不到
RETURNS_MOCKS 返回一个 mock 而不是空
RETURNS_DEEP_STUBS	可以连续点点儿调用多个方法
CALLS_REAL_METHODS 调用真实方法
	stub 时，使用 doXXX...when...methodCall() 不然真实方法就被调用了
RETURNS_SELF 针对 builder 方法，一直返回 this 的那种
	
```



针对  `RETURNS_SELF` 举例：

```java
 public class HttpRequesterWithHeaders {

      private HttpBuilder builder;

      public HttpRequesterWithHeaders(HttpBuilder builder) {
          this.builder = builder;
      }

      public String request(String uri) {
          return builder.withUrl(uri)
                  .withHeader("Content-type: application/json")
                  .withHeader("Authorization: Bearer")
                  .request();
      }
  }

  private static class HttpBuilder {

      private String uri;
      private List<String> headers;

      public HttpBuilder() {
          this.headers = new ArrayList<String>();
      }

       public HttpBuilder withUrl(String uri) {
           this.uri = uri;
           return this;
       }

       public HttpBuilder withHeader(String header) {
           this.headers.add(header);
           return this;
       }

       public String request() {
          return uri + headers.toString();
       }
  }

  // 测试
  @Test
  public void use_full_builder_with_terminating_method() {
      HttpBuilder builder = mock(HttpBuilder.class, RETURNS_SELF);
      HttpRequesterWithHeaders requester = new HttpRequesterWithHeaders(builder);
      String response = "StatusCode: 200";

      when(builder.request()).thenReturn(response);

      assertThat(requester.request("URI")).isEqualTo(response);
  }
 
```



再看个例子：

```java
class m {
    n nn;
    n getNn() {
        return nn;
    }
}
class n {
    int go() {
        return 1;
    }
}

@RunWith(MockitoJUnitRunner.class)
class Test {
    // 如果这 answer 使用默认值，则报一个普通的空指针
    // 如果是 Answers.RETURNS_SMART_NULLS，则报一个聪明的空指针
    // 如果是 RETURNS_DEEP_STUBS，则正常返回
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    m mm;
    
    @Test
    public void test() {
        when(mm.getNn().go()).thenReturn(1);
    }
}

```

***Please note that in most scenarios a mock returning a mock is wrong.***



#### Resetting Mock

Normally, you don't need to reset your mocks, just create new mocks for each test method

> The only reason we added `reset()` method is to make it possible to work with container-injected mocks

不推荐使用，但可以用

`Mockito. reset (T ... mocks)`



#### 限制

一下没法 mock，考虑使用 `powermock/jmockit`

- `final class/method`
- `enum`
- `static method`
- `private method`
- `hashCode/equals`



#### BDD 风格 mock

```java

 import static org.mockito.BDDMockito.*;

 Seller seller = mock(Seller.class);
 Shop shop = new Shop(seller);

 public void shouldBuyBread() throws Exception {
   //given
   given(seller.askForBread()).willReturn(new Bread());

   //when
   Goods goods = shop.buyBread();

   //then
   assertThat(goods, containBread());
 }
 
```

```java
 given(dog.bark()).willReturn(2);

 // when
 ...

 then(person).should(times(2)).ride(bike);
```



#### One-liner stubs 

```java
public class CarTest {
   Car boringStubbedCar = when(mock(Car.class).shiftGear())
       					  .thenThrow(EngineNotStarted.class)
                          .getMock();

   @Test public void should... {}
}
```



#### Verification ignoring stubs（没啥卵用）

配合 `verifyNoMoreInteractions` 和 `inOrder` 使用

```java
 verify(mock).foo();
 verify(mockTwo).bar();

 //ignores all stubbed methods:
 verifyNoMoreInteractions(ignoreStubs(mock, mockTwo));

 //creates InOrder that will ignore stubbed
 InOrder inOrder = inOrder(ignoreStubs(mock, mockTwo));
 inOrder.verify(mock).foo();
 inOrder.verify(mockTwo).bar();
 inOrder.verifyNoMoreInteractions();
```



#### 查看 mock details（给好事之人准备）

```java

   //To identify whether a particular object is a mock or a spy:
   Mockito.mockingDetails(someObject).isMock();
   Mockito.mockingDetails(someObject).isSpy();

   //Getting details like type to mock or default answer:
   MockingDetails details = mockingDetails(mock);
   details.getMockCreationSettings().getTypeToMock();
   details.getMockCreationSettings().getDefaultAnswer();

   //Getting invocations and stubbings of the mock:
   MockingDetails details = mockingDetails(mock);
   details.getInvocations();
   details.getStubbings();

   //Printing all interactions (including stubbing, unused stubs)
   System.out.println(mockingDetails(mock).printInvocations());
```



#### Delegate calls to real instance

方法调用代理给真实对象，无法记录它的调用，交互之类的

适用于：

- Final classes but with an interface
- Already custom proxied object
- Special objects with a finalize method, i.e. to avoid executing it 2 times

```java

   final class DontYouDareToMockMe implements list { ... }

   DontYouDareToMockMe awesomeList = new DontYouDareToMockMe();

   List mock = mock(List.class, delegatesTo(awesomeList));
```

```java

   List listWithDelegate = mock(List.class, AdditionalAnswers.delegatesTo(awesomeList));

   //Impossible: real method is called so listWithDelegate.get(0) throws IndexOutOfBoundsException (the list is yet empty)
   when(listWithDelegate.get(0)).thenReturn("foo");

   //You have to use doReturn() for stubbing
   doReturn("foo").when(listWithDelegate).get(0);
```



#### Mocking static methods (since 3.4.0)

使用 `mockStatic` api。

前面虽然通过加一个配置文件可以 mock final 和 enum，这里说的是static，用下面的方法：

```java
 assertEquals("foo", Foo.method());
 try (MockedStatic mocked = mockStatic(Foo.class)) {
     mocked.when(Foo::method).thenReturn("bar");
     assertEquals("bar", Foo.method());
     mocked.verify(Foo::method);
 }
 assertEquals("foo", Foo.method());
```

可以看到，有了 scope 的效果，里面 mock 外面和以前一样。



#### Mocking object construction (since 3.5.0)

是线程隔离的，不会影响其他线程的该类使用。

这个更他妈诡异，简直像黑魔法了：

```java
 assertEquals("foo", new Foo().method());
 try (MockedConstruction mocked = mockConstruction(Foo.class)) {
     Foo foo = new Foo();
     when(foo.method()).thenReturn("bar");
     assertEquals("bar", foo.method());
     verify(foo).method();
 }
 assertEquals("foo", new Foo().method());
```

可以看到里面的 Foo new 出来的和外面不一样！



#### 其它

- 创建类 `org.mockito.configuration.MockitoConfiguration` 可以覆盖 mockito 的默认配置

- 序列化一个 mock/spy

> The behaviour was implemented for a specific use case of a BDD spec that had an unreliable external dependency

```java
 List<Object> list = new ArrayList<Object>();
 // 还可以序列化 spy（没别的办法）
 List<Object> spy = mock(ArrayList.class, withSettings()
                 .spiedInstance(list)
                 .defaultAnswer(CALLS_REAL_METHODS)
                 .serializable());
 
```

-  支持跨 classloader 的序列化

```java
// use regular serialization 
mock(Book.class, withSettings().serializable());  
// use serialization across classloaders 
mock(Book.class, withSettings().serializable(ACROSS_CLASSLOADERS));
```

- Meta data and generic type retention (Since 2.1.0)

  现在 mock 之后的对象会正常保留注解和泛型！

- Mocking final types, enums and final methods (Since 2.1.0)

  本来是不支持的，现在如果你往根目录放一个这个就可以用了，以后不知道会变成什么样

  > just create in the classpath a file `/mockito-extensions/org.mockito.plugins.MockMaker` containing the value `mock-maker-inline`

- lenient() 

  默认是 strict 的，如果你 stub 了方法但是没用它，没 verify 它，会报错，使用下面方法改变这一行为：

  ```java
     lenient().when(mock.foo()).thenReturn("ok");
  ```

  或

  ```java
    Foo mock = Mockito.mock(Foo.class, withSettings().lenient());
  ```

  

- `MockitoFramework.clearInlineMocks()` 清除 inline mock 的状态，防止内存溢出

  如果你用了 inline mock，放到 `@After``@AfterEach` 方法里调用



- do not mock

  > When marking a type `@DoNotMock`, you should always point to alternative testing solutions such as standard fakes or other testing utilities. Mockito enforces `@DoNotMock` with the `DoNotMockEnforcer`. If you want to use a custom `@DoNotMock` annotation, the `DoNotMockEnforcer` will match on annotations with a type ending in "`org.mockito.DoNotMock`". You can thus place your custom annotation in `com.my.package.org.mockito.DoNotMock` and Mockito will enforce that types annotated by `@com.my.package.org.mockito.DoNotMock` can not be mocked

意思是自定义 `DoNotMock`注解，放到一个包里，以 `org.mockito.DoNotMock`结尾，就可以防止被 mock。

- `org.mockito.ArgumentMatchers`带的一些静态方法：

  > any, any, anyBoolean, anyByte, anyChar, anyCollection, anyDouble, anyFloat, anyInt, anyIterable, anyList, anyLong, anyMap, anySet, anyShort, anyString, argThat, booleanThat, byteThat, charThat, contains, doubleThat, endsWith, eq, eq, eq, eq, eq, eq, eq, eq, eq, floatThat, intThat, isA, isNotNull, isNull, longThat, matches, matches, notNull, nullable, refEq, same, shortThat, startsWith

- clearInvocations

  下面场景用：

  - You are using a dependency injection framework to inject your mocks.
  - The mock is used in a stateful scenario. For example a class is Singleton which depends on your mock.

- 线程安全？

  不要多线程 mock、stub、verify，这不对

- Law of Demeter 似懂非懂，看了如没看，不如不看

  又名 **principle of least knowledge**，有如下特点：

  - Each unit should have only limited knowledge about other units: only units "closely" related to the current unit.
  - Each unit should only talk to its friends; don't talk to strangers.
  - Only talk to your immediate friends.

> It may be viewed as a corollary to the principle of least privilege, which dictates that a module possess only the information and resources necessary for its legitimate purpose

a 可以调用 b 的方法，但不能通过 b 获取第三个对象 c，这样它就知道了 b 的内部构造。

#### 一些 api 讲解

##### doCallRealMethod

do 系列，针对 void 的

```java
   Foo mock = mock(Foo.class);
   doCallRealMethod().when(mock).someVoidMethod();

   // this will call the real implementation of Foo.someVoidMethod()
   mock.someVoidMethod();

```

##### doNothing

**Beware that void methods on mocks do nothing by default!**

连续调用：

```java
   doNothing().
   doThrow(new RuntimeException()).when(mock).someVoidMethod();

   //does nothing the first time:
   mock.someVoidMethod();

   //throws RuntimeException the next time:
   mock.someVoidMethod();
```

用在间谍上：

```java
   List list = new LinkedList();
   List spy = spy(list);

   //let's make clear() do nothing
   doNothing().when(spy).clear();

   spy.add("one");

   //clear() does nothing, so the list still contains "one"
   spy.clear();
```

##### ignoreStubs

对于 stub 过的方法，对它的调用也算一个 interaction，一般我们应当忽略这些 interaction，不过通常不需要忽略。如果你想 verifyNoMoreInteractions，但调用了 stub 方法，那会报错。

有三个地方可以忽略对  stub 方法的调用：

```java
  // inorder 中
  InOrder inOrder = inOrder(ignoreStubs(list));
  inOrder.verify(list).add(0);
  inOrder.verify(list).clear();
  inOrder.verifyNoMoreInteractions();
```

```java
// verify 的时候指定
verifyNoMoreInteractions(ignoreStubs(mock1, mock2))
```

使用 `Strictness.STRICT_STUBS`

```java
//junit4 中的 rule
@Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
// junit5 中使用类注解 @MockitoSettings，默认是 Strictness.STRICT_STUBS
```

##### calls

只能用在 inorder 验证里

> This verification mode can only be used with in order verification.



跟 `atLeast，times` 比，它比较独特，所以说说：

```java
inOrder.verify( mock, calls( 2 )).someMethod( "some arg" );
```

- will not fail if the method is called 3 times, unlike times( 2 )
- will not mark the third invocation as verified, unlike atLeast( 2 )

##### after(int time) 和 timeout(int time)

很明显，前者要等 time ms 之后验证结果；后者一旦发现有结果了立即返回，至多等 time ms 。

##### withSettings

重点是下面这句：

> Repeat after me: simple tests push simple, KISSy, readable & maintainable code

随便举个例子（没啥卵用）：

```java
//Creates mock with different default answer & name
   Foo mock = mock(Foo.class, withSettings()
       .defaultAnswer(RETURNS_SMART_NULLS)
       .name("cool mockie"));

   //Creates mock with different default answer, descriptive name and extra interfaces
   Foo mock = mock(Foo.class, withSettings()
       .defaultAnswer(RETURNS_SMART_NULLS)
       .name("cool mockie")
       .extraInterfaces(Bar.class));
```

##### description

前面提到过，补充 verify 的错误信息：

```java
 verify(mock, description("This will print on failure")).someMethod("some arg");
```

##### lenient

如果你 stub 了方法，但没用它，严格模式下会报错，此时可以用 lenient 模式。

***一般也是用不到，尽量别用！***

前面说过。三种办法：

1. rule/类注解里指定
2. mock 时指定

```java
foo = mock(Foo.class, withSettings().lenient())
```

3. 使用静态方法：

   ```java
   lenient().when(foo.foo()).thenReturn("ok")
   ```

   